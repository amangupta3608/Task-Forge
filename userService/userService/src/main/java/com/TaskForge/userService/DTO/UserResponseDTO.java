package com.TaskForge.userService.DTO;

import com.TaskForge.userService.ENUM.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private RoleType role;
    private String companyName;
}
