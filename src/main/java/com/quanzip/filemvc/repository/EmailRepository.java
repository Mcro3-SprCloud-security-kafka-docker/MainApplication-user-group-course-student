package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
