package com.digitalholics.securityservice.Security.Auth;

import com.digitalholics.securityservice.Security.Domain.Model.Entity.User;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.AuthResponse;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.LoginRequest;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.RegisterRequest;
import com.digitalholics.securityservice.Security.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/security/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @PostMapping(value = "authentication")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value="registration")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(value = "/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request,response);
    }

    @GetMapping(value="/findByUsername/{username}")
    public Optional<User> findUserByUsername(
            @PathVariable String username
    ){
        return authService.getByUsername(username);
    }

    @GetMapping("/validate-jwt")
    public String validateJwtAndReturnUsername(@RequestHeader("Authorization") String jwt) {

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita los primeros 7 caracteres ("Bearer ")
        }
        String username = jwtService.validateJwtAndGetUsername(jwt);
        return username != null ? username : jwt;
    }



}
