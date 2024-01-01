package com.joboffers;


import com.joboffers.infrastructure.security.jwt.JwtConfigurationProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@SpringBootApplication
@Log4j2
//@EnableRedisRepositories
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class JobOffersApplication {

    public static void main(String[] args) {
        log.info("--- Starting application ---");
        SpringApplication.run(JobOffersApplication.class, args);
    }

}


