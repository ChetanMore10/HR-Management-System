package com.hrms.service;

import com.hrms.dto.LoginRequest;
import com.hrms.dto.LoginResponse;
import com.hrms.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    LoginResponse login(LoginRequest request);
    String register(RegisterRequest request);
    String logout();
}
