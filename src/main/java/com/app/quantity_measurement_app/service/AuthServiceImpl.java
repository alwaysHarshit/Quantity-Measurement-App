package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.entity.User;
import com.app.quantity_measurement_app.model.LoginRequest;
import com.app.quantity_measurement_app.model.ResponseDTO;
import com.app.quantity_measurement_app.model.SignupRequest;
import com.app.quantity_measurement_app.repository.UserRepository;
import com.app.quantity_measurement_app.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseDTO signup(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseDTO.error(400, "Email already exists");
        }
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(user);
        return ResponseDTO.success("User registered successfully", null);
    }

    @Override
    public ResponseDTO login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String token = jwtUtils.generateToken(user.getEmail());
                return ResponseDTO.success("Login successful", token);
            }
        }
        return ResponseDTO.error(401, "Invalid email or password");
    }
}
