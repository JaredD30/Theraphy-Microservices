package com.digitalholics.profileservice.Profile.resource;

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
}
