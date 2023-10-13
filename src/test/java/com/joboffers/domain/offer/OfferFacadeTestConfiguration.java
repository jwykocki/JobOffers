package com.joboffers.domain.offer;

import java.util.List;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import java.util.List;

public class OfferFacadeTestConfiguration {

    private final InMemoryFetcherTestImpl inMemoryFetcherTest;
    private final InMemoryOfferRepository offerRepository;

    OfferFacadeTestConfiguration() {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(
                List.of(
                        new JobOfferResponse("title", "name", "salary", "url1"),
                        new JobOfferResponse("title", "name", "salary", "url2"),
                        new JobOfferResponse("title", "name", "salary", "url3"),
                        new JobOfferResponse("title", "name", "salary", "url4"),
                        new JobOfferResponse("title", "name", "salary", "url5"),
                        new JobOfferResponse("title", "name", "salary", "url6")
                )
        );
        this.offerRepository = new InMemoryOfferRepository();
    }

    OfferFacadeTestConfiguration(List<JobOfferResponse> remoteClientOffers) {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(remoteClientOffers);
        this.offerRepository = new InMemoryOfferRepository();
    }

    OfferFacade offerFacadeForTests() {
        return new OfferFacade(offerRepository, new OfferService(inMemoryFetcherTest, offerRepository));
    }
}