package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import com.joboffers.domain.offer.dto.OfferRequestDto;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;


public class OfferFacadeTest {

    @Test
    public void should_fetch_jobs_from_remote_and_save_all_offers_when_repository_is_empty() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();

        // when
        List<OfferResponseDto> result = offerFacade.fetchAllOffersAndSaveAllIfNotExists();

        // then
        assertThat(result).hasSize(6);
    }

    @Test
    public void should_fetch_0_jobs_from_remote_and_save_0_offers_when_repository_is_empty() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();

        // when
        List<OfferResponseDto> result = offerFacade.fetchAllOffersAndSaveAllIfNotExists();

        // then
        assertThat(result).hasSize(0);
    }

    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_urls() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
                List.of(
                        new JobOfferResponse("title", "name", "salary", "url1"),
                        new JobOfferResponse("title", "name", "salary", "url2"),
                        new JobOfferResponse("title", "name", "salary", "url3"),
                        new JobOfferResponse("title", "name", "salary", "url4"),
                        new JobOfferResponse("newTitle", "newName", "newSalary", "new@url1"),
                        new JobOfferResponse("newTitle", "newName", "newSalary", "new@url2")
                )
        ).offerFacadeForTests();
        offerFacade.saveOffer(new OfferRequestDto("title", "name", "salary", "url1"));
        offerFacade.saveOffer(new OfferRequestDto("title", "name", "salary", "url2"));
        offerFacade.saveOffer(new OfferRequestDto("title", "name", "salary", "url3"));
        offerFacade.saveOffer(new OfferRequestDto("title", "name", "salary", "url4"));
        assertThat(offerFacade.findAllOffers()).hasSize(4);

        // when
        List<OfferResponseDto> response = offerFacade.fetchAllOffersAndSaveAllIfNotExists();

        // then
        assertThat(List.of(
                        response.get(0).offerUrl(),
                        response.get(1).offerUrl()
                )
        ).containsExactlyInAnyOrder("new@url1", "new@url2");
    }

    @Test
    public void should_save_4_offers_when_there_are_no_offers_in_database() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();

        // when
        offerFacade.saveOffer(new OfferRequestDto("name", "title", "salary", "url1"));
        offerFacade.saveOffer(new OfferRequestDto("name", "title", "salary", "url2"));
        offerFacade.saveOffer(new OfferRequestDto("name", "title", "salary", "url3"));
        offerFacade.saveOffer(new OfferRequestDto("name", "title", "salary", "url4"));

        // then
        assertThat(offerFacade.findAllOffers()).hasSize(4);
    }

    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(new OfferRequestDto("name", "title", "salary", "url1"));
        // when
        OfferResponseDto offerById = offerFacade.findOfferById(offerResponseDto.id());

        // then
        assertThat(offerById).isEqualTo(OfferResponseDto.builder()
                .id(offerResponseDto.id())
                .companyName("name")
                .position("title")
                .salary("salary")
                .offerUrl("url1")
                .build()
        );
    }

    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();

        // when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById("100"));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 100 not found");
    }

    @Test
    public void should_throw_duplicate_key_exception_when_with_offer_url_exists() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(new OfferRequestDto("name", "title", "salary", "url5"));
        String savedId = offerResponseDto.id();
        assertThat(offerFacade.findOfferById(savedId).id()).isEqualTo(savedId);
        // when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(
                new OfferRequestDto("name", "title", "salary", "url5")));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferDuplicateException.class)
                .hasMessage("Offer with offerUrl [url5] already exists");
    }
}