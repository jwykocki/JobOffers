package com.joboffers.domain.loginandregister;

import com.joboffers.domain.loginandregister.dto.RegisterUserDto;
import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import com.joboffers.domain.loginandregister.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.UUID;


@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private static final String USER_NOT_FOUND = "User not found";
    private final LoginRepository loginRepository;

    public UserDto findByUsername(String username) {
        return loginRepository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.username(), user.password()))
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND));
    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        final User user = RegisterUserDtoMapper.mapFromRegisterUserDto(
                UUID.randomUUID().toString(), registerUserDto);
        User savedUser = loginRepository.save(user);
        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());

    }
}
