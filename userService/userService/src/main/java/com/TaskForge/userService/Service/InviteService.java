package com.TaskForge.userService.Service;

import com.TaskForge.userService.ENUM.RoleType;

public interface InviteService {
    void sendInvite(String email, RoleType role, String companyId);
    boolean validateToken(String token);
}
