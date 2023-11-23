package com.joboffers.infrastructure.loginandregister.controller;

import com.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import com.joboffers.domain.loginandregister.dto.RegisterUserDto;
import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Log4j2
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        log.info("Received request - POST '/register' - username: " + registerUserDto.username());
        String encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registrationResult = loginAndRegisterFacade.register(
                new RegisterUserDto(registerUserDto.username(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResult);


    }
}
