package com.joboffers.infrastructure.offer.controller;

import com.joboffers.domain.offer.OfferFacade;
import com.joboffers.domain.offer.dto.OfferRequestDto;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@Log4j2
public class OfferRestController {

    private final OfferFacade offerFacade;


    @GetMapping("/offers")
    public ResponseEntity<List<OfferResponseDto>> findAllOffers() {
        log.info("Received request - GET '/offers'");
        List<OfferResponseDto> results = offerFacade.findAllOffers();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/offers/{uniqueId}")
    public ResponseEntity<OfferResponseDto> findOfferById(@PathVariable String uniqueId) {
        log.info("Received request - GET '/offers/" + uniqueId + "'");
        OfferResponseDto result = offerFacade.findOfferById(uniqueId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/offers")
    public ResponseEntity<OfferResponseDto> saveOffer(@RequestBody @Valid OfferRequestDto offerDto) {
        log.info("Received request - POST '/offers'");
        OfferResponseDto result = offerFacade.saveOffer(offerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
