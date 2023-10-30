package com.joboffers.infrastructure.offer.controller;

import com.joboffers.domain.offer.OfferFacade;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@AllArgsConstructor
public class OfferRestController {

    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferResponseDto>> findAllOffers() {
        List<OfferResponseDto> results = offerFacade.findAllOffers();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{uniqueId}")
    public ResponseEntity<OfferResponseDto> findOfferById(@PathVariable String uniqueId) {
        OfferResponseDto result = offerFacade.findOfferById(uniqueId);
        return ResponseEntity.ok(result);
    }
}