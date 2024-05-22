package com.digitalholics.securityservice.Security.Resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResource {
    Integer id;
    String firstname;
    String lastname;
    String username;
    String password;
    String role;
}
