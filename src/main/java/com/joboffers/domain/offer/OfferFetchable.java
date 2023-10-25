package com.joboffers.domain.offer;

import java.util.Arrays;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import org.springframework.context.annotation.Bean;

import java.util.List;


public interface OfferFetchable {
    List<JobOfferResponse> fetchOffers();
}
