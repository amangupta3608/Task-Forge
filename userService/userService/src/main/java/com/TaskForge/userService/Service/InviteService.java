package com.TaskForge.userService.Service;

import com.TaskForge.userService.ENUM.RoleType;
import com.TaskForge.userService.Model.InviteToken;

import java.util.UUID;

public interface InviteService {

    /**
     * Sends an invite email with a unique token.
     *
     * @param email     The invitee's email.
     * @param role      The role the user is invited for.
     * @param companyId The ID of the company sending the invite.
     * @return The invite token as a String.
     */
    String sendInvite(String email, RoleType role, UUID companyId);

    /**
     * Validates if the token exists and is not expired or already accepted.
     *
     * @param token The invite token.
     * @return True if valid, false otherwise.
     */
    boolean validateToken(UUID token);

    /**
     * Retrieves the InviteToken details.
     *
     * @param token The invite token.
     * @return The corresponding InviteToken object.
     */
    InviteToken getInviteToken(UUID token);

    /**
     * Marks the token as accepted (used).
     *
     * @param token The invite token.
     */
    void markTokenAsAccepted(UUID token);
}
