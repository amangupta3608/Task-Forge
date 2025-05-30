package com.TaskForge.userService.Service.impl;

import com.TaskForge.userService.DTO.UserRegistrationDTO;
import com.TaskForge.userService.Exception.ResourceNotFoundException;
import com.TaskForge.userService.Model.Company;
import com.TaskForge.userService.Model.InviteToken;
import com.TaskForge.userService.Model.User;
import com.TaskForge.userService.Repository.CompanyRepository;
import com.TaskForge.userService.Repository.InviteRepository;
import com.TaskForge.userService.Repository.UserRepository;
import com.TaskForge.userService.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Override
    public User registerUser(UserRegistrationDTO dto, UUID inviteTokenId){
        InviteToken invite = inviteRepository.findById(inviteTokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));

        if(invite.isAccepted()){
            throw new IllegalStateException("Invite already accepted");
        }
        Company company = companyRepository.findById(invite.getCompany().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Company not found"));

        User user = User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(dto.getRole())
                .company(company)
                .createdAt(LocalDateTime.now())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        invite.setAccepted(true);
        inviteRepository.save(invite);

        return savedUser;
    }
}
