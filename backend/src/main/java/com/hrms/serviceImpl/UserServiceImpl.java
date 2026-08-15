package com.hrms.serviceImpl;

import com.hrms.dto.LoginRequest;
import com.hrms.dto.LoginResponse;
import com.hrms.dto.RegisterRequest;
import com.hrms.entity.User;
import com.hrms.repository.UserRepository;
import com.hrms.service.AuthService;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements AuthService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new LoginResponse(
                null,
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public String register(RegisterRequest request) {

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        userRepo.save(user);

        return "User registered successfully";
    }

    @Override
    public String logout() {
        return "Logout successful";
    }
}