package com.TaskForge.userService.DTO;

import com.TaskForge.userService.ENUM.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRegistrationDTO {
    @Email@NotBlank
    private String email;

    @NotBlank
    private String firstName;

    private String lastName;


    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    @NotNull
    private RoleType role;

    @NotNull
    private UUID inviteTokenId;
}
