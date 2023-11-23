package com.joboffers.infrastructure.offer.controller.error;

import com.joboffers.domain.offer.OfferNotFoundException;
import com.joboffers.infrastructure.loginandregister.controller.error.TokenErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

@ControllerAdvice()
@Log4j2
public class OfferControllerErrorHandler {



    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferErrorResponse handleOfferNotFoundException(OfferNotFoundException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new OfferErrorResponse(message, HttpStatus.NOT_FOUND);
    }





}
