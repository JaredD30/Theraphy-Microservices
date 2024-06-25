package com.digitalholics.therapyservice.Therapy.domain.model.entity.External;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    Integer id;
    String firstname;
    String lastname;
    String username;
    String password;
    String role;
}