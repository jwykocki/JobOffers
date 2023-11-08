package com.joboffers;


import com.joboffers.infrastructure.offer.http.OfferHttpClientConfigurationProperties;
import com.joboffers.infrastructure.security.jwt.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties(value = {OfferHttpClientConfigurationProperties.class, JwtConfigurationProperties.class})
public class JobOffersApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersApplication.class, args);
    }

}


