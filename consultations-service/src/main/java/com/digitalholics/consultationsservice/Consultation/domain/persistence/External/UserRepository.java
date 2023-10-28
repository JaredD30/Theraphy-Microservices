package com.digitalholics.consultationsservice.Consultation.domain.persistence.External;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

}
