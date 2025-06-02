package com.TaskForge.userService.Repository;

import com.TaskForge.userService.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);
//    Optional<Company> findById(UUID id);
}
