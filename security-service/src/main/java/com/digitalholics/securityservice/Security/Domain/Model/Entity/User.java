package com.digitalholics.securityservice.Security.Domain.Model.Entity;

import com.digitalholics.securityservice.Security.Domain.Model.Enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Schema(description = "User Model Information")
public class User implements UserDetails {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User Id", example = "123")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Schema(description = "User's firstname", example = "John")
    String firstname;

    @Schema(description = "User's lastname", example = "Doe")
    String lastname;

    @Schema(description = "User's username", example = "john@theraphy.com")
    @Column(unique = true)
    String username;

    @Schema(description = "User's password", example = "132456*Co")
    String password;

    @Schema(description = "User's role", example = "PATIENT")
    @Enumerated(EnumType.STRING)
    Role role;


    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    //@OneToOne(mappedBy = "user")
    //private Patient patient;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return List.of(new SimpleGrantedAuthority((role.name())));
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
