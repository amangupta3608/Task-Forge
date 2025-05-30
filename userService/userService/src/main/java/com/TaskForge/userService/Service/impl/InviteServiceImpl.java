package com.TaskForge.userService.Service.impl;

import com.TaskForge.userService.Model.InviteToken;
import com.TaskForge.userService.ENUM.RoleType;
import com.TaskForge.userService.Repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.TaskForge.userService.Exception.ResourceNotFoundException;
import com.TaskForge.userService.Service.InviteService;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {
    private final InviteRepository inviteRepository;

    @Override
    public void sendInvite(String email, RoleType role, String companyId){
        String token = UUID.randomUUID().toString();

        InviteToken invite = InviteToken.builder()
                .email(email)
                .role(role)
                .companyId(UUID.fromString(companyId))
                .token(token)
                .expiryDate(LocalDateTime.now().plusDays(2))
                .accepted(false)
                .build();

        inviteRepository.save(invite);

        System.out.println("Invite token generated for: " + email);
        System.out.println("Invite link: http://localhost:8080/invite/validate?token=" + token);
    }

    @Override
    public boolean validateToken(String token) {
        InviteToken invite = inviteRepository.findByInviteToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

        if(invite.isAccepted()){
            throw new IllegalStateException("Token already used");
        }

        if(invite.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token expired");
        }
        return true;
    }

    public InviteToken getInviteToken(String token){
        InviteToken invite = inviteRepository.findByInviteToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid invite token"));
        if(invite.isAccepted() || invite.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token expired or already used");
        }
        return invite;
    }

    public void markTokenAsAccepted(String token){
        InviteToken invite = inviteRepository.findByInviteToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Token not found"));
        invite.setAccepted(true);
        inviteRepository.save(invite);
    }

}
