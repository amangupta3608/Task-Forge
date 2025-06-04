package com.TaskForge.userService.Controller;

import com.TaskForge.userService.ENUM.RoleType;
import com.TaskForge.userService.Service.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<String> inviteUser(@RequestParam String email,
                                            @RequestParam RoleType role,
                                            @RequestParam UUID companyId){
        inviteService.sendInvite(email, role, companyId);
        return ResponseEntity.ok("InviteToken sent to " + email);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateInvite(@RequestParam UUID token){
        boolean isValid = inviteService.validateToken(token);
        return ResponseEntity.ok(isValid ? "Token is valid" : "Token is invalid");
    }
}
