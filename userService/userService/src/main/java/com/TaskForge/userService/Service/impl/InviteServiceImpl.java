package com.TaskForge.userService.Service.impl;

import com.TaskForge.userService.ENUM.RoleType;
import com.TaskForge.userService.Exception.ResourceNotFoundException;
import com.TaskForge.userService.Model.Company;
import com.TaskForge.userService.Model.InviteToken;
import com.TaskForge.userService.Repository.CompanyRepository;
import com.TaskForge.userService.Repository.InviteRepository;
import com.TaskForge.userService.Service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.xdev.brevo.api.TransactionalEmailsApi;
import software.xdev.brevo.model.SendSmtpEmail;
import software.xdev.brevo.model.SendSmtpEmailSender;
import software.xdev.brevo.model.SendSmtpEmailToInner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;
    private final CompanyRepository companyRepository;
    private final TransactionalEmailsApi transactionalEmailsApi;

    @Override
    public String sendInvite(String email, RoleType role, UUID companyId) {
        log.info("Attempting to send invite to email: {}, role: {}, companyId: {}", email, role, companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for id: " + companyId));

        // Prevent multiple pending invites for same email
        if (inviteRepository.existsByEmailAndAcceptedFalse(email)) {
            log.warn("Invite already sent to email {} and not accepted yet", email);
            throw new IllegalStateException("Invite already sent and not yet accepted");
        }

        InviteToken invite = InviteToken.builder()
                .email(email)
                .role(role)
                .company(company)
                .token(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(2))
                .accepted(false)
                .build();

        inviteRepository.save(invite);

        // Send email invite
        sendInviteEmail(email, invite.getToken());

        log.info("Invite sent successfully to {}, token: {}", email, invite.getToken());

        return invite.getToken().toString(); // ✅ Fix: Convert UUID to String
    }

    private void sendInviteEmail(String email, UUID token) {
        try {
            String link = "https://localhost:8080/invites/validate?token=" + token;

            SendSmtpEmailToInner to = new SendSmtpEmailToInner().email(email);

            SendSmtpEmail emailRequest = new SendSmtpEmail()
                    .subject("You're invited to Planora")
                    .htmlContent("<p>You're invited to join Planora</p>" +
                            "<p>Click the link below to register:</p>" +
                            "<a href=\"" + link + "\">Accept Invite</a>")
                    .to(Arrays.asList(to))
                    .sender(new SendSmtpEmailSender().name("Planora Team").email("no-reply@planora.com"));

            transactionalEmailsApi.sendTransacEmail(emailRequest);
            log.debug("Invite email sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Failed to send invite email to {}", email, e);
            throw new RuntimeException("Failed to send invite email", e); // ✅ Fix: Complete message
        }
    }

    @Override
    public boolean validateToken(UUID token) {
        return inviteRepository.findByToken(token)
                .map(invite -> {
                    boolean valid = !invite.isAccepted()
                            && (invite.getExpiryDate() == null || invite.getExpiryDate().isAfter(LocalDateTime.now()));
                    log.info("Token {} validation result: {}", token, valid);
                    return valid;
                })
                .orElseGet(() -> {
                    log.warn("Token not found or invalid: {}", token);
                    return false;
                });
    }

    @Override
    public InviteToken getInviteToken(UUID token) {
        InviteToken invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid invite token"));

        if (invite.isAccepted() || invite.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Token expired or already used: {}", token);
            throw new IllegalStateException("Token expired or already used");
        }

        return invite;
    }

    @Override
    public void markTokenAsAccepted(UUID token) {
        InviteToken invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        invite.setAccepted(true);
        inviteRepository.save(invite);

        log.info("Token marked as accepted: {}", token);
    }
}
