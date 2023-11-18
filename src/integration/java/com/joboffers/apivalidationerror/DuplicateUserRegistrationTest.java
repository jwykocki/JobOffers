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
public class DuplicateUserRegistrationTest extends BaseIntegrationTest {


    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void should_return_409_and_message_when_user_already_exists() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUsername",
                        "password": "somePassword"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isCreated()).andReturn();

        ResultActions performUseExists = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUsername",
                        "password": "somePassword"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult mvcResultUserExists = performUseExists.andExpect(status().isConflict()).andReturn();
        String json = mvcResultUserExists.getResponse().getContentAsString();
        RegisterErrorResponse result = objectMapper.readValue(json, RegisterErrorResponse.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "User already exists");
        assertThat(result.httpStatus()).isEqualTo(HttpStatus.CONFLICT);
    }
}

