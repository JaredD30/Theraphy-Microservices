package com.digitalholics.securityservice.Security.Auth;

import com.digitalholics.securityservice.Security.Domain.Model.Entity.User;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.AuthResponse;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.LoginRequest;
import com.digitalholics.securityservice.Security.Domain.Service.Communication.RegisterRequest;
import com.digitalholics.securityservice.Security.Jwt.JwtService;
import com.digitalholics.securityservice.Security.Resource.UserResource;
import com.digitalholics.securityservice.Security.mapping.UserMapper;
import com.digitalholics.securityservice.Shared.Exception.ResourceValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication, registration, and token validation")
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    private final UserMapper mapper;

    @Operation(summary = "Login user", description = "Returns access_token and refresh_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully login"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @PostMapping(value = "authentication")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Registration user", description = "Returns access_token and refresh_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully register "),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping(value="registration")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Refresh Token", description = "Creates a new access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed "),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping(value = "/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request,response);
    }

    @Operation(summary = "Search user by username", description = "Returns user with a provide username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping(value="/findByUsername/{username}")
    public Optional<User> findUserByUsername(
            @Parameter(description = "User's username", required = true, examples = @ExampleObject(name = "Username", value = "luis@email.com")) @PathVariable String username
    ){
        return authService.getByUsername(username);
    }

    @Operation(summary = "Validate JSON Web Token", description = "Returns username from jwt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/validate-jwt")
    public String validateJwtAndReturnUsername( @Parameter(hidden = true) @RequestHeader("Authorization") String jwt) {

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita los primeros 7 caracteres ("Bearer ")
        }
        String username = jwtService.validateJwtAndGetUsername(jwt);

        return username;
    }

    @GetMapping("/get-user")
    public UserResource validateJwtAndReturnUser(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt) {

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita los primeros 7 caracteres ("Bearer ")
        }
        User username = jwtService.validateJwtAndGetUser(jwt);
        return username != null ? mapper.toResource(username) : null;
    }

    @GetMapping("/user/{id}")
    public UserResource getUserById(
            @PathVariable Integer id
    ) {
        return mapper.toResource(authService.getByUserId(id));
    }

}
