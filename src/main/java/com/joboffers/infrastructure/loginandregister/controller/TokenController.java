package com.joboffers.infrastructure.loginandregister.controller;

import com.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.joboffers.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import com.joboffers.infrastructure.security.jwt.JwtAuthenticatorFacade;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Log4j2
public class TokenController {

    private final JwtAuthenticatorFacade jwtAuthenticationFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto loginRequest) {
        log.info("Received request - POST '/token' - username: " + loginRequest.username());
        final JwtResponseDto jwtResponse = jwtAuthenticationFacade.authenticateAndGenerateToken(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
