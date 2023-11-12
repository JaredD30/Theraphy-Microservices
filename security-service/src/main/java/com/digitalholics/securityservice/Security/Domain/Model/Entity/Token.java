package com.digitalholics.securityservice.Security.Domain.Model.Entity;

import com.digitalholics.securityservice.Security.Domain.Model.Enumeration.TokenType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "Token Model Information")
public class Token {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Token Id", example = "123")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Token's token value", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdWlzQGVtYWlsLmNvbSIsImlhdCI6MTY5OTcwMDAxMCwiZXhwIjoxNjk5Nzg2NDEwfQ.XmWTUQSeRJe70fR0ua1kg0b3UhrLAeiXxtcqJFbyLlk")
    private String token;

    @Schema(description = "Token's type", example = "BEARER")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Schema(description = "Token's status for expiration", example = "true")
    private boolean expired;

    @Schema(description = "Token's status for revoke", example = "true")
    private boolean revoked;

    @Schema(description = "Token's user", example = "BEARER")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
