package com.tweetapp.userservice.controller;

import com.tweetapp.userservice.entity.User;
import com.tweetapp.userservice.payload.*;
import com.tweetapp.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin()
@RequestMapping("/api/v1.0/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = {"/login"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        LoginResponse response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"/register"} )
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDto registerDto) {
        RegisterResponse response = authService.register(registerDto);
        if(response.getUser() == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/{username}/forgot"})
    public ResponseEntity<PasswordResponse> forgotPassword(@PathVariable("username") String username,
                                               @RequestBody PasswordDto passwordDto) {
        PasswordResponse response = authService.resetPassword(username, passwordDto);
        if(response.getUser() == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/users/all"})
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = {"/user/search/{username}"})
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        Optional<User> user = authService.getUserByUsername(username);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = {"/user/search/email/{email}"})
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        Optional<User> user = authService.getUserByEmail(email);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = {"/user/search/id/{id}"})
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = authService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

}
