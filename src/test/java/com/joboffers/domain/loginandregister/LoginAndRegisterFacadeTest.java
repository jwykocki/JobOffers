package com.joboffers.domain.loginandregister;

import com.joboffers.domain.loginandregister.dto.RegisterUserDto;
import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import com.joboffers.domain.loginandregister.dto.UserDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterFacadeTest {

    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(new InMemoryLoginRepositoryTestImpl());



    @Test
    public void should_register_user() {
        // given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");


        // when
        RegistrationResultDto registrationResultDto = loginAndRegisterFacade.register(registerUserDto);

        // then
        assertAll(
                ()-> assertThat(registrationResultDto.id()).isNotNull(),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.username()).isEqualTo(registerUserDto.username())
        );

    }
    @Test
    public void should_find_user_by_username() {
        // given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        RegistrationResultDto registrationResultDto = loginAndRegisterFacade.register(registerUserDto);

        // when
        UserDto userDto = loginAndRegisterFacade.findByUsername(registerUserDto.username());

        // then
        assertThat(userDto).isEqualTo(new UserDto(registrationResultDto.id(), registerUserDto.username(), registerUserDto.password()));

    }

    @Test
    public void should_throw_exception_when_user_not_found() {
        // given
        String username = "username";

        // when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.findByUsername(username));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
    @Test
    public void should_throw_exception_when_cannot_save_in_database() {
        // given
        RegisterUserDto registerUserDto = null;

        // when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.register(registerUserDto));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(Exception.class);
    }


}