package com.TaskForge.userService.DTO;

import com.TaskForge.userService.ENUM.RoleType;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String inviteToken;
    private String email;
    private RoleType role;
    private String firstName;
    private String lastName;
    private String password;
}
