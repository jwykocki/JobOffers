package com.joboffers.infrastructure.error;

import com.joboffers.infrastructure.loginandregister.controller.error.RegisterErrorResponse;
import com.joboffers.infrastructure.loginandregister.controller.error.TokenErrorResponse;
import com.joboffers.infrastructure.offer.controller.error.OfferPostErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

@Log4j2
@ControllerAdvice
public class GlobalDuplicateKeyExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public GlobalErrorResponse globalDuplicate(DuplicateKeyException duplicateKeyException) {
        log.error("Duplicate key exception");
        if (duplicateKeyException.getMessage().contains("username")) {
            log.error("Duplicate username exception");
            return userDuplicate(duplicateKeyException);
        }
        else if (duplicateKeyException.getMessage().contains("url")){
            return offerDuplicate(duplicateKeyException);
        }

        else
            return new GlobalErrorResponse(Collections.singletonList(duplicateKeyException.getMessage()), HttpStatus.CONFLICT);
    }


    @ResponseBody
    public GlobalErrorResponse userDuplicate(DuplicateKeyException duplicateUserException) {
        log.error("Duplicate username exception");
        final String message = "User already exists";
        log.error(message);
        return new GlobalErrorResponse(Collections.singletonList(message), HttpStatus.CONFLICT);

    }

    @ResponseBody
    public GlobalErrorResponse offerDuplicate(DuplicateKeyException duplicateKeyException) {
        final String message = "Offer URL already exists";
        log.error(message);
        return new GlobalErrorResponse(Collections.singletonList(message), HttpStatus.CONFLICT);
    }

}
