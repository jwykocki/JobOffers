package com.joboffers.domain.loginandregister.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public record RegisterUserDto(
        @NotBlank(message = "{username.not.blank}")
        String username,
        @NotBlank(message = "{password.not.blank}")
                @Size(min = 6, max = 20, message = "{password.size.message}")
        String password) {
}
