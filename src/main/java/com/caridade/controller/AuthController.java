package com.caridade.controller;

import com.caridade.dto.request.AuthLoginRequestDTO;
import com.caridade.dto.response.AuthLoginResponseDTO;
import com.caridade.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthLoginResponseDTO login(@RequestBody @Valid AuthLoginRequestDTO request) {
        return authService.login(request);
    }
}