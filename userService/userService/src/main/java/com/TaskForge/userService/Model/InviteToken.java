package com.TaskForge.userService.Model;

import com.TaskForge.userService.ENUM.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invite_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteToken {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean accepted;

//    @Column(nullable = false)
//    private UUID companyId;

    @PrePersist
    protected void prePersist() {
        if(expiryDate == null) {
            this.expiryDate = LocalDateTime.now().plusDays(7);
        }
    }
}
