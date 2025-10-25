package com.solopilot.user.repository;

import com.autopilot.models.payload.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByUuid(String uuid);

    Optional<Contact> findByEmail(String email);
}
