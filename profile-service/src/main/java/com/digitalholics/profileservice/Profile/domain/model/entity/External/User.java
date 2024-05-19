package com.digitalholics.profileservice.Profile.domain.model.entity.External;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    String firstname;
    String lastname;
    String username;
    String password;
}
