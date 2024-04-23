package com.tweetapp.userservice.service.impl;

import com.tweetapp.userservice.entity.Role;
import com.tweetapp.userservice.entity.User;
import com.tweetapp.userservice.payload.*;
import com.tweetapp.userservice.repository.RoleRepository;
import com.tweetapp.userservice.repository.UserRepository;
import com.tweetapp.userservice.security.JwtTokenProvider;
import com.tweetapp.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setMessage("Login successful!");
        return response;
    }

    @Override
    public RegisterResponse register(RegisterDto registerDto) {
        RegisterResponse response = new RegisterResponse();
        // add check for username existing in database
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            response.setMessage("Username already exists in database");
            response.setUser(null);
            return response;
        }
        // add check for email existing in database
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            response.setMessage("Email already exists in database");
            response.setUser(null);
            return response;
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setContactNumber(registerDto.getContactNumber());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // assign roles to the user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles_ids(roles);

        // save user into database
        User savedUser = userRepository.save(user);

        response.setMessage("User has been successfully registered");
        response.setUser(savedUser);
        return response;
    }

    @Override
    public PasswordResponse resetPassword(String username, PasswordDto passwordDto) {
        User user = userRepository.findByUsername(username).get();
        PasswordResponse response = new PasswordResponse();
        if(!passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) {
            response.setMessage("The old password field does not match the stored database password for this user");
            response.setUser(null);
            return response;
        }
        if(!passwordEncoder.matches(passwordDto.getConfirmPassword(), user.getPassword())) {
            response.setMessage("The confirm password field does not match the stored database password for this user");
            response.setUser(null);
            return response;
        }
        if(passwordDto.getNewPassword().length() >= 8) {
            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
            User savedUser = userRepository.save(user);
            response.setMessage("User's password has been successfully changed");
            response.setUser(savedUser);
            return response;
        } else {
            throw new RuntimeException("Password does not meet requirements");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }


}
