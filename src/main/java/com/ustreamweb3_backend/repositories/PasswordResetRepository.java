package com.ustreamweb3_backend.repositories;

import com.ustreamweb3_backend.entities.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer>{
    Optional<PasswordReset> findByToken(String token);

}
