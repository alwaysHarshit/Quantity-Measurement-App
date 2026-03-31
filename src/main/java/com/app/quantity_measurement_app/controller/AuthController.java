package com.app.quantity_measurement_app.controller;

import com.app.quantity_measurement_app.model.LoginRequest;
import com.app.quantity_measurement_app.model.ResponseDTO;
import com.app.quantity_measurement_app.model.SignupRequest;
import com.app.quantity_measurement_app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (e.g. email already exists)",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
            })
    public ResponseEntity<ResponseDTO> signup(@Valid @RequestBody SignupRequest request) {
        ResponseDTO response = authService.signup(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized (invalid credentials)",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
            })
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginRequest request) {
        ResponseDTO response = authService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
