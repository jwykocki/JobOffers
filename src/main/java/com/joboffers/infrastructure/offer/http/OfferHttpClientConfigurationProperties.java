package com.joboffers.infrastructure.offer.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "offer.http.client.config")
@Builder
public record OfferHttpClientConfigurationProperties(int connectionTimeout, int readTimeout, int port, String uri){

}
