package com.caridade.service;

import com.caridade.dto.request.AuthLoginRequestDTO;
import com.caridade.dto.response.AuthLoginResponseDTO;

public interface AuthService {

    AuthLoginResponseDTO login(AuthLoginRequestDTO request);
}