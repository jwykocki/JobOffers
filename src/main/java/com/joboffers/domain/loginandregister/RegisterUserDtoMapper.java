package com.joboffers.domain.loginandregister;

import com.joboffers.domain.loginandregister.dto.RegisterUserDto;

public class RegisterUserDtoMapper {
    public static User mapFromRegisterUserDto(String generatedId, RegisterUserDto userDto) {
        return User.builder()
                .id(generatedId)
                .username(userDto.username())
                .password(userDto.password())
                .build();
    }
}
