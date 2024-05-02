package com.tweetapp.userservice.service;

import com.tweetapp.userservice.entity.User;
import com.tweetapp.userservice.payload.*;

import java.util.List;
import java.util.Optional;

public interface AuthService {
    LoginResponse login(LoginDto loginDto);
    RegisterResponse register(RegisterDto registerDto);
    PasswordResponse resetPassword(String username, PasswordDto passwordDto);
    List<User> getAllUsers();

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(Long id);
}
