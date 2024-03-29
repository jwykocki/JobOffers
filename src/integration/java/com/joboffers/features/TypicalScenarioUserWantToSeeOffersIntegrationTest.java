package com.joboffers.features;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.joboffers.BaseIntegrationTest;
import com.joboffers.SampleJobOfferResponse;
import java.util.List;
import java.util.regex.Pattern;

import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import com.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.joboffers.infrastructure.offer.scheduler.HttpOffersScheduler;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class TypicalScenarioUserWantToSeeOffersIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offer.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("offer.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Autowired
    HttpOffersScheduler httpOffersScheduler;


    @Test
    public void user_want_to_see_offers_but_have_to_be_logged_in_and_external_server_should_have_some_offers() throws Exception {

        final String urlOfferTemplate = "/offers";

        //step 1: there are no offers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));


        //step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given && when
        List<OfferResponseDto> newZeroOffers = httpOffersScheduler.deleteOldAndfetchNewOffers();
        // then
        assertThat(newZeroOffers).isEmpty();


        //step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given && when
        ResultActions performFailedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                         {
                         "username": "someUser",
                         "password": "somePassword"
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        performFailedLoginRequest.andExpect(status()
                        .isUnauthorized())
                        .andExpect(content().json("""
                                {
                                   "message": "Bad Credentials",
                                   "status": "UNAUTHORIZED"
                                }
                                """.trim()
                        ));


        //step 4: user made GET /offers with no jwt token and system returned FORBIDDEN(403
        // given && when
        ResultActions performGetOffersUnauthorized = mockMvc.perform(get(urlOfferTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        performGetOffersUnauthorized.andExpect(status().isForbidden());


        //step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status CREATED(201)
        // given & when
        ResultActions performRegister = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult registerActionResult = performRegister.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull()
        );


        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        ResultActions performSuccessLogin = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult successLoginResult = performSuccessLogin.andExpect(status().isOk()).andReturn();
        String successLoginResultJson = successLoginResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(successLoginResultJson, JwtResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );


        //step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // given && when
        ResultActions performGetZeroOffers = mockMvc.perform(get(urlOfferTemplate)
                        .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcresultZeroOffers = performGetZeroOffers.andExpect(status().isOk()).andReturn();
        String jsonWithOffers = mvcresultZeroOffers.getResponse().getContentAsString();
        List <OfferResponseDto> zeroOffers = objectMapper.readValue(jsonWithOffers, new TypeReference<>(){
        });
        assertThat(zeroOffers).isEmpty();


        //step 8: there are 2 new offers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersJson())));


        //step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // given && when
        List<OfferResponseDto> newTwoOffers = httpOffersScheduler.deleteOldAndfetchNewOffers();
        // then
        assertThat(newTwoOffers).hasSize(2);


        //step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        // given && when
        ResultActions performGetTwoOffers = mockMvc.perform(get(urlOfferTemplate)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResultTwoOffers = performGetTwoOffers.andExpect(status().isOk()).andReturn();
        String jsonWithTwoOffers = mvcResultTwoOffers.getResponse().getContentAsString();
        List <OfferResponseDto> twoOffers = objectMapper.readValue(jsonWithTwoOffers, new TypeReference<>(){
        });
        assertThat(twoOffers).hasSize(2);
        OfferResponseDto expectedFirstOffer = newTwoOffers.get(0);
        OfferResponseDto expectedSecondOffer = newTwoOffers.get(1);
        assertThat(twoOffers).containsExactlyInAnyOrder(
                new OfferResponseDto(expectedFirstOffer.id(), expectedFirstOffer.companyName(), expectedFirstOffer.position(), expectedFirstOffer.salary(), expectedFirstOffer.offerUrl()),
                new OfferResponseDto(expectedSecondOffer.id(), expectedSecondOffer.companyName(), expectedSecondOffer.position(), expectedSecondOffer.salary(), expectedSecondOffer.offerUrl())
        );


        //step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // given
        String notExistingId = "/9999";
        // when
        ResultActions performGetOffersNotExistingId = mockMvc.perform(get(urlOfferTemplate+notExistingId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        performGetOffersNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                           "message": "Offer with id 9999 not found.",
                           "status": "NOT_FOUND"
                        }
                        """.trim()
                ));


        //step 12: user made GET /offers/1000 and system returned OK(200) with offer
        // given
        String exampleExistingId = expectedFirstOffer.id();
        // when
        ResultActions performGetOffersExampleExistingId = mockMvc.perform(get(urlOfferTemplate+'/'+exampleExistingId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        performGetOffersExampleExistingId.andExpect(status().isOk());


        //step 13: there are 4 offers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersJson())));


        //step 14: scheduler ran 3rd time and made GET to external server and system added 4 new offers with ids: 3000 and 4000 to database
        // given && when
        List<OfferResponseDto> newOffers = httpOffersScheduler.deleteOldAndfetchNewOffers();
        // then
        assertThat(newOffers).hasSize(4);


        //step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        // given && when
        ResultActions performGetFourOffers = mockMvc.perform(get(urlOfferTemplate)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcresultFourOffers = performGetFourOffers.andExpect(status().isOk()).andReturn();
        String jsonWithFourOffers = mvcresultFourOffers.getResponse().getContentAsString();
        List <OfferResponseDto> fourOffers = objectMapper.readValue(jsonWithFourOffers, new TypeReference<>(){
        });
        assertThat(fourOffers).hasSize(4);
        OfferResponseDto expectedFirstOfferFrom4 = newOffers.get(0);
        OfferResponseDto expectedSecondOfferFrom4 = newOffers.get(1);
        OfferResponseDto expectedThirdOfferFrom4 = newOffers.get(2);
        OfferResponseDto expectedFourthOfferFrom4 = newOffers.get(3);
        assertThat(fourOffers).containsExactlyInAnyOrder(
                new OfferResponseDto(expectedFirstOfferFrom4.id(), expectedFirstOfferFrom4.companyName(), expectedFirstOfferFrom4.position(), expectedFirstOfferFrom4.salary(), expectedFirstOfferFrom4.offerUrl()),
                new OfferResponseDto(expectedSecondOfferFrom4.id(), expectedSecondOfferFrom4.companyName(), expectedSecondOfferFrom4.position(), expectedSecondOfferFrom4.salary(), expectedSecondOfferFrom4.offerUrl()),
                new OfferResponseDto(expectedThirdOfferFrom4.id(), expectedThirdOfferFrom4.companyName(), expectedThirdOfferFrom4.position(), expectedThirdOfferFrom4.salary(), expectedThirdOfferFrom4.offerUrl()),
                new OfferResponseDto(expectedFourthOfferFrom4.id(), expectedFourthOfferFrom4.companyName(), expectedFourthOfferFrom4.position(), expectedFourthOfferFrom4.salary(), expectedFourthOfferFrom4.offerUrl())
        );


        //step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        ResultActions performPostOffer = mockMvc.perform(post(urlOfferTemplate)
                .content("""
                         {
                         "companyName": "someCompanyName",
                         "position": "somePosition",
                         "salary": "someSalary",
                         "offerUrl": "someOfferUrl"
                         }
                        """.trim())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        String createdOfferJson = performPostOffer.andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OfferResponseDto parsedCreatedOfferJson = objectMapper.readValue(createdOfferJson, OfferResponseDto.class);
        String id = parsedCreatedOfferJson.id();
        assertAll(
                () -> assertThat(parsedCreatedOfferJson.offerUrl()).isEqualTo("someOfferUrl"),
                () -> assertThat(parsedCreatedOfferJson.companyName()).isEqualTo("someCompanyName"),
                () -> assertThat(parsedCreatedOfferJson.salary()).isEqualTo("someSalary"),
                () -> assertThat(parsedCreatedOfferJson.position()).isEqualTo("somePosition"),
                () -> assertThat(id).isNotNull()
        );


        //step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 5 offers
        ResultActions performGetOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String offersJson = performGetOffers.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<OfferResponseDto> parsedJsonWithOffers = objectMapper.readValue(offersJson, new TypeReference<>() {
        });
        assertThat(parsedJsonWithOffers).hasSize(5);
        assertThat(parsedJsonWithOffers.stream().map(OfferResponseDto::id)).contains(id);
    }
}