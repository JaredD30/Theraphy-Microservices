package com.digitalholics.securityservice.Security.Domain.Service.Communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @JsonProperty("access_Token")
    private String accessToken;

    @JsonProperty("refresh_Token")
    private String refreshToken;
}
