package com.TaskForge.userService.DTO;

import com.TaskForge.userService.ENUM.RoleType;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleType role;
}
