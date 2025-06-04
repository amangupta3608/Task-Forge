package com.TaskForge.userService.Controller;

import com.TaskForge.userService.DTO.LoginRequestDTO;
import com.TaskForge.userService.DTO.UserRegistrationDTO;
import com.TaskForge.userService.DTO.UserResponseDTO;
import com.TaskForge.userService.Model.User;
import com.TaskForge.userService.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO dto) {
        UserResponseDTO registeredUser = userService.registerUser(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered with ID: " + registeredUser.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            String jwt = userService.loginUser(loginRequest);
            return ResponseEntity.ok(jwt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
