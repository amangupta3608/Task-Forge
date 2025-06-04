package com.TaskForge.userService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @Email
    private String email;
    @Size(min = 8, max = 30)
    private String password;
}
