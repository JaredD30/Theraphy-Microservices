package com.digitalholics.securityservice.Security.Auth;

import com.digitalholics.securityservice.Security.Domain.Model.Entity.User;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.AuthResponse;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.LoginRequest;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;



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



}
