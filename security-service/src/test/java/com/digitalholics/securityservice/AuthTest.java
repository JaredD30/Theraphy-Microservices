package com.digitalholics.securityservice;

import com.digitalholics.securityservice.Security.Auth.AuthController;
import com.digitalholics.securityservice.Security.Auth.AuthService;
import com.digitalholics.securityservice.Security.Domain.Model.Entity.User;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.AuthResponse;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.LoginRequest;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.RegisterRequest;
import com.digitalholics.securityservice.Security.Jwt.JwtService;
import com.digitalholics.securityservice.Security.Resource.UserResource;
import com.digitalholics.securityservice.Security.mapping.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class AuthTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        AuthResponse response = new AuthResponse();
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = new AuthResponse();
        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testFindUserByUsername() {
        User user = new User();
        when(authService.getByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = authController.findUserByUsername("username");

        assertEquals(user, result.get());
    }

    @Test
    void testValidateJwtAndReturnUsername() {
        String jwt = "Bearer token";
        String username = "user";
        when(jwtService.validateJwtAndGetUsername(anyString())).thenReturn(username);

        String result = authController.validateJwtAndReturnUsername(jwt);

        assertEquals(username, result);
    }

    @Test
    void testValidateJwtAndReturnUser() {
        String jwt = "Bearer token";
        User user = new User();
        UserResource userResource = new UserResource();
        when(jwtService.validateJwtAndGetUser(anyString())).thenReturn(user);
        when(mapper.toResource(any(User.class))).thenReturn(userResource);

        UserResource result = authController.validateJwtAndReturnUser(jwt);

        assertEquals(userResource, result);
    }

    @Test
    void testGetUserById() {
        User user = new User();
        UserResource userResource = new UserResource();
        when(authService.getByUserId(anyInt())).thenReturn(user);
        when(mapper.toResource(any(User.class))).thenReturn(userResource);

        UserResource result = authController.getUserById(1);

        assertEquals(userResource, result);
    }
}
