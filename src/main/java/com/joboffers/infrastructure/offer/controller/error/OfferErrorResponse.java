package com.joboffers.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record OfferErrorResponse(String message, HttpStatus status) {

    public static OfferErrorResponse of(String message, HttpStatus status) {
        return new OfferErrorResponse(message, status);
    }
}
