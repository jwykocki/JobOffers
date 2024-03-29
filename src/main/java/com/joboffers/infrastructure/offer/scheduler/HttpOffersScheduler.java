package com.joboffers.infrastructure.offer.scheduler;

import com.joboffers.domain.offer.OfferFacade;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class HttpOffersScheduler {

    private final OfferFacade offerFacade;
    private static final String STARTED_OFFERS_FETCHING_MESSAGE = "Started offers fetching {}";
    private static final String STOPPED_OFFERS_FETCHING_MESSAGE = "Stopped offers fetching {}";
    private static final String ADDED_NEW_OFFERS_MESSAGE = "Added new {} offers";
    private static final String DELETED_OFFERS_FROM_DATABASE_MESSAGE = "Deleted offers from database {}";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelayString = "${http.offers.scheduler.request.delay}")
    public List<OfferResponseDto> deleteOldAndfetchNewOffers(){
        log.info(DELETED_OFFERS_FROM_DATABASE_MESSAGE, dateFormat.format(new Date()));
        offerFacade.deleteOffersFromDatabase();
        log.info(STARTED_OFFERS_FETCHING_MESSAGE, dateFormat.format(new Date()));
        final List<OfferResponseDto> addedOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExists();
        log.info(ADDED_NEW_OFFERS_MESSAGE, addedOffers.size());
        log.info(STOPPED_OFFERS_FETCHING_MESSAGE, dateFormat.format(new Date()));
        return addedOffers;
    }
}