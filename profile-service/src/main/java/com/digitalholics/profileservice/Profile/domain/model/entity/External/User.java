package com.digitalholics.profileservice.Profile.domain.model.entity.External;


import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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
