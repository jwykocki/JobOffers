package com.joboffers.infrastructure.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record GlobalErrorResponse(

    List<String> messages,
    HttpStatus httpStatus

){ }
