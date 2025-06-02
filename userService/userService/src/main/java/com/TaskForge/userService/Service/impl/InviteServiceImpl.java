package com.TaskForge.userService.Service.impl;

import com.TaskForge.userService.Model.Company;
import com.TaskForge.userService.Model.InviteToken;
import com.TaskForge.userService.ENUM.RoleType;
import com.TaskForge.userService.Repository.CompanyRepository;
import com.TaskForge.userService.Repository.InviteRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.TaskForge.userService.Exception.ResourceNotFoundException;
import com.TaskForge.userService.Service.InviteService;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {
    private final InviteRepository inviteRepository;
    private final JavaMailSenderImpl mailSender;
    private final CompanyRepository companyRepository;

    @Override
    public void sendInvite(String email, RoleType role, String companyId){
        Company company = companyRepository.findById(UUID.fromString(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if(inviteRepository.existsByEmailAndAcceptedFalse(email)){
            throw new IllegalStateException("Invite already sent and not yet accepted");
        }
        String token = UUID.randomUUID().toString();

        InviteToken invite = InviteToken.builder()
                .email(email)
                .role(role)
                .company(company)
                .token(token)
                .expiryDate(LocalDateTime.now().plusDays(2))
                .accepted(false)
                .build();

        inviteRepository.save(invite);

        sendInviteEmail(email, token);

    }
    private void sendInviteEmail(String email, String token) {
        try {
            String link = "https://localhost:8080/invites/validate?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("You're invited to Planora");
            helper.setText("<p>You've been invited to join Planora</p>" +
                    "<p>Click the link below to register:</p>" +
                    "<a href=\"" + link + "\">Accept Invite</a>", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invite email", e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        InviteToken invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

        if(invite.getExpiryDate().isBefore(LocalDateTime.now()) || invite.isAccepted()){
            return false;
        }
        return true;
    }

    public InviteToken getInviteToken(String token){
        InviteToken invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid invite token"));
        if(invite.isAccepted() || invite.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token expired or already used");
        }
        return invite;
    }

    public void markTokenAsAccepted(String token){
        InviteToken invite = inviteRepository.findByToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Token not found"));
        invite.setAccepted(true);
        inviteRepository.save(invite);
    }

}
