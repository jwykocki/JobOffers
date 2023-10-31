package com.joboffers.domain.offer.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record OfferRequestDto(
        @NotEmpty(message = "{companyName.not.empty}")
        @NotNull(message = "{companyName.not.null}")
        String companyName,
        @NotEmpty(message = "{position.not.empty}")
        @NotNull(message = "{position.not.null}")
        String position,
        @NotEmpty(message = "{salary.not.empty}")
        @NotNull(message = "{salary.not.null}")
        String salary,
        @NotEmpty(message = "{offerUrl.not.empty}")
        @NotNull(message = "{offerUrl.not.null}")
        String offerUrl
) {
}