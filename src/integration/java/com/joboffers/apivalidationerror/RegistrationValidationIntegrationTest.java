package com.joboffers.apivalidationerror;

import com.joboffers.BaseIntegrationTest;
import com.joboffers.infrastructure.loginandregister.controller.error.RegisterErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class RegistrationValidationIntegrationTest extends BaseIntegrationTest {
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void should_return_400_and_message_when_username_is_blank() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "",
                        "password": "longpassword"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "username must not be blank");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void should_return_400_and_message_when_password_is_blank() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "username",
                        "password": ""
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "password must not be blank",
                    "password must be between 6 and 20 characters");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void should_return_400_and_message_when_password_is_not_blank_but_under_6_characters() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "username",
                        "password": "pwd"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "password must be between 6 and 20 characters");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void should_return_400_and_message_when_password_is_not_blank_but_over_20_characters() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "username",
                        "password": "longpasswordover20characters"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "password must be between 6 and 20 characters");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void should_return_400_and_message_when_username_is_blank_and_password_is_under_6_characters() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "",
                        "password": "pswd"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "username must not be blank",
                "password must be between 6 and 20 characters");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void should_return_400_and_message_when_both_username_and_password_are_blank() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "",
                        "password": ""
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "password must be between 6 and 20 characters",
                "password must not be blank",
                "username must not be blank");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }




}
