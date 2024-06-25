package com.digitalholics.securityservice.Security.Domain.Persistence;


import com.digitalholics.securityservice.Security.Domain.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findUserById(Integer id);
}
