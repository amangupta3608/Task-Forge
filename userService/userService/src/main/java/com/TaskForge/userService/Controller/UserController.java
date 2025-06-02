package com.TaskForge.userService.Controller;

import com.TaskForge.userService.DTO.LoginRequestDTO;
import com.TaskForge.userService.DTO.UserRegistrationDTO;
import com.TaskForge.userService.Model.User;
import com.TaskForge.userService.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO dto,
                                               @RequestParam UUID inviteTokenId) {
        User registerdUser = userService.registerUser(dto, inviteTokenId);
        return ResponseEntity.ok("User registered with ID: " + registerdUser.getId());
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String jwt = userService.loginUser(loginRequest);
            return ResponseEntity.ok(jwt);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(401).body("Invalid Credentials");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Something went wronng");
        }
    }

}
