package com.joboffers.infrastructure.offer.http;

import com.joboffers.domain.offer.OfferFetchable;
import java.time.Duration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@AllArgsConstructor
public class OfferHttpClientConfig {

//    private final OfferHttpClientConfigurationProperties properties;


//    @Bean(name = "offerHttpClientConfigurationProperties")
//    public OfferHttpClientConfigurationProperties offerHttpClientConfigurationProperties(){
//    return OfferHttpClientConfigurationProperties.builder()
//                .connectionTimeout(properties.connectionTimeout())
//                .readTimeout(properties.readTimeout())
//                .port(properties.port())
//                .uri(properties.uri())
//                .build();
//    }


    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(@Value("${offer.http.client.config.connectionTimeout:1000}") long connectionTimeout,
                                     @Value("${offer.http.client.config.readTimeout:1000}") long readTimeout,
                                     RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    public OfferFetchable remoteOfferClient(RestTemplate restTemplate,
                                            @Value("${offer.http.client.config.uri}") String uri,
                                            @Value("${offer.http.client.config.port}") int port ){
        return new OfferHttpClient(restTemplate, uri, port);
    }

//    @Bean
//    public OfferFetchable remoteOfferClient(RestTemplate restTemplate,
//                                            properties.uri(),
//                                            @Value("${offer.http.client.config.port}") int port ){
//        return new OfferHttpClient(restTemplate, uri, port);
//    }


}