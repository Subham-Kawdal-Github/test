package com.incident.management.service;

import com.incident.management.dto.ForgotPasswordRequest;
import com.incident.management.dto.LoginRequest;
import com.incident.management.dto.UserDto;
import com.incident.management.entity.User;

public interface UserService {
    UserDto register(UserDto userDto);
    String login(LoginRequest loginRequest);
    void forgotPassword(ForgotPasswordRequest request);
    UserDto getUserById(Long userId);
    User getCurrentUser();
}
