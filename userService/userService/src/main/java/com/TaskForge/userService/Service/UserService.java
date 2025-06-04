package com.TaskForge.userService.Service;

import com.TaskForge.userService.DTO.LoginRequestDTO;
import com.TaskForge.userService.DTO.UserRegistrationDTO;
import com.TaskForge.userService.DTO.UserResponseDTO;
import com.TaskForge.userService.Model.User;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO dto);
    String loginUser(LoginRequestDTO loginRequest);
}
