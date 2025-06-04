package com.TaskForge.userService.Repository;

import com.TaskForge.userService.Model.InviteToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<InviteToken, UUID> {
    Optional<InviteToken> findByToken(UUID token);
    Optional<InviteToken> findByEmail(String email);
    boolean existsByEmailAndAcceptedFalse(String email);
}
