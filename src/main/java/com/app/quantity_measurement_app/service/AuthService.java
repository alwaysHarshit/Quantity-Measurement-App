package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.model.LoginRequest;
import com.app.quantity_measurement_app.model.ResponseDTO;
import com.app.quantity_measurement_app.model.SignupRequest;

public interface AuthService {
    ResponseDTO signup(SignupRequest signupRequest);
    ResponseDTO login(LoginRequest loginRequest);
}
