package com.digitalholics.securityservice.Security.Domain.Service.Communication;


import com.digitalholics.securityservice.Security.Domain.Model.Enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String username;
    String password;
    String firstname;
    String lastname;
    private Role role;
}
