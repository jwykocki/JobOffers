package com.joboffers.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record RegisterErrorResponse (

    List<String> messages,
    HttpStatus httpStatus
        ){
}
