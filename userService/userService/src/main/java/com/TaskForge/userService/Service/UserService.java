package com.TaskForge.userService.Service;

import com.TaskForge.userService.DTO.UserRegistrationDTO;
import com.TaskForge.userService.Model.User;

import java.util.UUID;

public interface UserService {
    User registerUser(UserRegistrationDTO dto, UUID inviteTokenId);
}
