package com.tweetapp.userservice.service;

import com.tweetapp.userservice.entity.User;
import com.tweetapp.userservice.payload.*;

import java.util.List;

public interface AuthService {
    LoginResponse login(LoginDto loginDto);
    RegisterResponse register(RegisterDto registerDto);
    PasswordResponse resetPassword(String username, PasswordDto passwordDto);
    List<User> getAllUsers();

    User getUserByUsername(String username);

    User getUserByEmail(String email);
}
