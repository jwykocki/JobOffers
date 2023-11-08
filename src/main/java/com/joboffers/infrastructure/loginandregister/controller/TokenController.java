package com.joboffers.infrastructure.loginandregister.controller;

import com.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.joboffers.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import com.joboffers.infrastructure.security.jwt.JwtAuthenticatorFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class TokenController {

    private final JwtAuthenticatorFacade jwtAuthenticationFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto loginRequest) {
        final JwtResponseDto jwtResponse = jwtAuthenticationFacade.authenticateAndGenerateToken(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
