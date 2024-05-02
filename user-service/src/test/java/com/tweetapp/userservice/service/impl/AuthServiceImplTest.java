package com.tweetapp.userservice.service.impl;


import com.netflix.discovery.converters.Auto;
import com.tweetapp.userservice.entity.Role;
import com.tweetapp.userservice.entity.User;
import com.tweetapp.userservice.payload.*;
import com.tweetapp.userservice.repository.RoleRepository;
import com.tweetapp.userservice.repository.UserRepository;
import com.tweetapp.userservice.security.JwtTokenProvider;
import com.tweetapp.userservice.service.AuthService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.beans.BeanUtils.copyProperties;

@SpringBootTest(properties = "eureka.client.enabled=false")
public class AuthServiceImplTest {

    private AuthService authService;
    private User user;
    private User user2;
    private RegisterDto registerDto = new RegisterDto();
    private LoginDto loginDto = new LoginDto();
    private PasswordDto passwordDto = new PasswordDto();
    private Role userRole;
    private String encodedPassword = "asdfjkl;";
    private String encodedPassword2 = ";lkjfdsa";
    private List<User> userList = new ArrayList<>();

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach()
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authService = new AuthServiceImpl(userRepository,
                roleRepository, passwordEncoder, authenticationManager,
                jwtTokenProvider);

        user = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRole = Role.builder()
                .name("ROLE_USER")
                .build();

        passwordDto = PasswordDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .newPassword("13243546")
                .build();

        copyProperties(user, registerDto);

        userList.add(user);
        userList.add(user2);
    }

    @AfterEach
    public void tearDown() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            userRepository.delete(user);
        }
        roleRepository.delete(userRole);
    }

    @DisplayName("Register User Service JUnit Success Test")
    @Test
    public void givenRegisterDto_whenRegistered_thenReturnSuccessResponse() {
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.existsByUsername(registerDto.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(registerDto.getEmail())).willReturn(false);
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(userRole));
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword);


        RegisterResponse response = authService.register(registerDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("User has been successfully registered");
        assertThat(response.getUser()).isNotNull();
    }

    @DisplayName("Register User Service JUnit Test Exists By Username Failed")
    @Test
    public void givenRegisterDto_whenExistsByUsername_thenReturnFailureResponse() {
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.existsByUsername(registerDto.getUsername())).willReturn(true);
        given(userRepository.existsByEmail(registerDto.getEmail())).willReturn(false);
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(userRole));
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword);


        RegisterResponse response = authService.register(registerDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Username already exists in database");
        assertThat(response.getUser()).isNull();
    }

    @DisplayName("Register User Service JUnit Test Exists By Email Failed")
    @Test
    public void givenRegisterDto_whenExistsByEmail_thenReturnFailureResponse() {
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.existsByUsername(registerDto.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(registerDto.getEmail())).willReturn(true);
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(userRole));
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword);


        RegisterResponse response = authService.register(registerDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Email already exists in database");
        assertThat(response.getUser()).isNull();
    }

    @DisplayName("Login User Service JUnit Test Success")
    @Test
    public void givenLoginDto_whenLogin_thenReturnSuccessResponse() {
        loginDto.setUsernameOrEmail(user.getUsername());
        loginDto.setPassword(encodedPassword);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);
        given(jwtTokenProvider.generateToken(authentication)).willReturn("qweruiop");

        LoginResponse response = authService.login(loginDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Login successful!");
        assertThat(response.getToken()).isNotNull();
    }

    @DisplayName("Login User Service JUnit Test Failed")
    @Test
    public void givenLoginDto_whenLoginWithInvalidCredentials_thenReturn401Unauthorized() {
        loginDto.setUsernameOrEmail(user.getUsername());
        loginDto.setPassword(encodedPassword);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(new BadCredentialsException("Unauthorized"));
        given(jwtTokenProvider.generateToken(authentication)).willReturn("qweruiop");

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginDto);
        });
    }

    @DisplayName("Reset Password User Service JUnit Test Success")
    @Test
    public void givenPasswordDto_whenResetPassword_thenReturnPasswordResponse() {
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(passwordDto.getConfirmPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword2);

        PasswordResponse response = authService.resetPassword(user.getUsername(), passwordDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("User's password has been successfully changed");
        assertThat(response.getUser()).isNotNull();
    }

    @DisplayName("Reset Password User Service JUnit Test Failed Invalid Get Password")
    @Test
    public void givenPasswordDto_whenResetPasswordWithInvalidGetPassword_thenReturnFailedPasswordResponse() {
        passwordDto.setPassword("asdfasdf");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(passwordDto.getConfirmPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword2);

        PasswordResponse response = authService.resetPassword(user.getUsername(), passwordDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("The old password field does not match the stored database password for this user");
        assertThat(response.getUser()).isNull();
    }

    @DisplayName("Reset Password User Service JUnit Test Failed Invalid Confirm Password")
    @Test
    public void givenPasswordDto_whenResetPasswordWithInvalidConfirmPassword_thenReturnFailedPasswordResponse() {
        passwordDto.setConfirmPassword("asdfasdf");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(passwordDto.getConfirmPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword2);

        PasswordResponse response = authService.resetPassword(user.getUsername(), passwordDto);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("The confirm password field does not match the stored database password for this user");
        assertThat(response.getUser()).isNull();
    }

    @DisplayName("Reset Password User Service JUnit Test Failed Invalid New Password")
    @Test
    public void givenPasswordDto_whenResetPasswordWithInvalidNewPassword_thenReturnFailedPasswordResponse() {
        passwordDto.setNewPassword("asdf");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(passwordDto.getConfirmPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(registerDto.getPassword())).willReturn(encodedPassword2);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.resetPassword(user.getUsername(), passwordDto);
        });
        assertThat(exception.getMessage()).isEqualTo("Password does not meet requirements");
    }

    @DisplayName("Get All Users User Service JUnit Test Success")
    @Test
    public void givenUserList_whenGetAllUsers_thenReturnUserList() {
        given(userRepository.findAll()).willReturn(userList);

        userList = authService.getAllUsers();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(0).getFirstName()).isEqualTo("Mark");
        assertThat(userList.get(1).getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Get All Users User Service JUnit Test Failed")
    @Test
    public void givenNoUsers_whenGetAllUsers_thenReturnEmptyList() {
        given(userRepository.findAll()).willReturn(new ArrayList<>());

        userList = authService.getAllUsers();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(0);
    }

    @DisplayName("Get User By Username User Service JUnit Test Success")
    @Test
    public void givenUsername_whenGetUserByUsername_thenReturnUserObject() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepository.findByUsername(user2.getUsername())).willReturn(Optional.of(user2));

        User foundUser1 = authService.getUserByUsername(user.getUsername()).get();
        User foundUser2 = authService.getUserByUsername(user2.getUsername()).get();

        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser1.getFirstName()).isEqualTo("Mark");
        assertThat(foundUser2.getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Get User By Username User Service JUnit Test Failed")
    @Test
    public void givenInvalidUsername_whenGetUserByUsername_thenReturnNullPointerException() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepository.findByUsername(user2.getUsername())).willReturn(null);

        User foundUser1 = authService.getUserByUsername(user.getUsername()).get();

        assertThrows(NullPointerException.class, () -> {
            User foundUser2 = authService.getUserByUsername(user2.getUsername()).get();
        });
    }

    @DisplayName("Get User By Email User Service JUnit Test Success")
    @Test
    public void givenEmail_whenGetUserByEmail_thenReturnUserObject() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(userRepository.findByEmail(user2.getEmail())).willReturn(Optional.of(user2));

        User foundUser1 = authService.getUserByEmail(user.getEmail()).get();
        User foundUser2 = authService.getUserByEmail(user2.getEmail()).get();

        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser1.getFirstName()).isEqualTo("Mark");
        assertThat(foundUser2.getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Get User By Email User Service JUnit Test Failed")
    @Test
    public void givenInvalidEmail_whenGetUserByEmail_thenReturnNullPointerException() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(userRepository.findByEmail(user2.getEmail())).willReturn(null);

        User foundUser1 = authService.getUserByEmail(user.getEmail()).get();

        assertThrows(NullPointerException.class, () -> {
            User foundUser2 = authService.getUserByEmail(user2.getEmail()).get();
        });
    }
}
