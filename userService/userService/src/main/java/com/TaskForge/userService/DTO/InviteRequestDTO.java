package com.TaskForge.userService.DTO;

import com.TaskForge.userService.ENUM.RoleType;
import lombok.Data;

import java.util.UUID;

@Data
public class InviteRequestDTO {
    private String email;
    private RoleType role;
    private UUID companyId;
}
