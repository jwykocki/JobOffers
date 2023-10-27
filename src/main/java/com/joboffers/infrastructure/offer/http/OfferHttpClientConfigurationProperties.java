package com.joboffers.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "offer.http.client.config")
@Builder
public record OfferHttpClientConfigurationProperties(int connectionTimeout, int readTimeout, int port, String uri){

}
