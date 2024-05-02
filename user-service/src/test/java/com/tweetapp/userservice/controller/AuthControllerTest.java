package com.tweetapp.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.userservice.entity.User;
import com.tweetapp.userservice.payload.*;
import com.tweetapp.userservice.repository.UserRepository;
import com.tweetapp.userservice.security.JwtTokenProvider;
import com.tweetapp.userservice.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthService authService;

    private User user;
    private User user2;
    private List<User> users = new ArrayList<>();
    private RegisterDto registerDto = new RegisterDto();
    private LoginDto loginDto = new LoginDto();
    private PasswordDto passwordDto = new PasswordDto();
    private String encodedPassword = "asdfjkl;asdfjkl;";

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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

        copyProperties(user, registerDto);
        loginDto.setUsernameOrEmail(user.getUsername());
        loginDto.setPassword(encodedPassword);

        passwordDto = PasswordDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .newPassword("13243546")
                .build();
    }

    @DisplayName("POST /register 201 Response")
    @Test
    public void givenRegisterDto_whenRegister_thenReturnRegisterResponseSuccess() throws Exception {
        RegisterResponse regResponse = new RegisterResponse("User has been successfully registered", user);

        given(authService.register(any(RegisterDto.class)))
                .willReturn(regResponse);

        ResultActions response = mockMvc.perform(post("/api/v1.0/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.user.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.user.email", is(user.getEmail())));
    }

    @DisplayName("POST /register 400 Response")
    @Test
    public void givenInvalidRegisterDto_whenRegister_thenReturnRegisterResponseSuccess() throws Exception {
        RegisterResponse regResponse = new RegisterResponse("Username already exists in database", null);

        given(authService.register(any(RegisterDto.class)))
                .willReturn(regResponse);

        ResultActions response = mockMvc.perform(post("/api/v1.0/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(regResponse.getMessage())));
    }

    @DisplayName("POST /login 200 Response")
    @Test
    public void givenLoginDto_whenLogin_thenReturnLoginResponseSuccess() throws Exception {
        LoginResponse loginResponse = new LoginResponse("Login successful!", "asdfjkl;");

        given(authService.login(any(LoginDto.class)))
                .willReturn(loginResponse);

        ResultActions response = mockMvc.perform(post("/api/v1.0/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(loginResponse.getMessage())))
                .andExpect(jsonPath("$.token", is(loginResponse.getToken())));
    }

    @DisplayName("POST /{username}/forgot 200 Response")
    @Test
    public void givenPasswordDto_whenResetPassword_thenReturnPasswordResponseSuccess() throws Exception {
        PasswordResponse passwordResponse = new PasswordResponse("User's password has been successfully changed", user);

        given(authService.resetPassword(eq(user.getUsername()), any(PasswordDto.class)))
                .willReturn(passwordResponse);

        ResultActions response = mockMvc.perform(post("/api/v1.0/auth/{username}/forgot", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDto)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.user.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.user.email", is(user.getEmail())));
    }

    @DisplayName("POST /{username}/forgot 400 Response")
    @Test
    public void givenInvalidPasswordDto_whenResetPassword_thenReturnPasswordResponseFailed() throws Exception {
        PasswordResponse passwordResponse = new PasswordResponse("The old password field does not match the stored database password for this user", null);

        given(authService.resetPassword(eq(user.getUsername()), any(PasswordDto.class)))
                .willReturn(passwordResponse);

        ResultActions response = mockMvc.perform(post("/api/v1.0/auth/{username}/forgot", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(passwordResponse.getMessage())));
    }

    @DisplayName("GET /users/all Empty List")
    @Test
    public void givenUserObjects_whenGetAllUsers_thenReturnEmptyList() throws Exception {
        given(authService.getAllUsers())
                .willReturn(Collections.emptyList());

        ResultActions response = mockMvc.perform(get("/api/v1.0/auth/users/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @DisplayName("GET /users/all Full List")
    @Test
    public void givenUserObjects_whenGetAllUsers_thenReturnAllEmployees() throws Exception {
        users.add(user);
        users.add(user2);
        given(authService.getAllUsers())
                .willReturn(users);

        ResultActions response = mockMvc.perform(get("/api/v1.0/auth/users/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(user.getLastName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[1].firstName", is(user2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(user2.getLastName())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail())));
    }

    @DisplayName("GET /user/search/{username} 200 Found")
    @Test
    public void givenUsername_whenGetUserByUsername_thenReturnUserObject() throws Exception {
        given(authService.getUserByUsername(any(String.class)))
                .willReturn(Optional.of(user));

        ResultActions response = mockMvc.perform(get("/api/v1.0/auth/user/search/{username}", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @DisplayName("GET /user/search/email/{email} 200 Found")
    @Test
    public void givenUserEmail_whenGetUserByEmail_thenReturnUserObject() throws Exception {
        given(authService.getUserByEmail(any(String.class)))
                .willReturn(Optional.of(user));

        ResultActions response = mockMvc.perform(get("/api/v1.0/auth/user/search/email/{email}", user.getEmail())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

}
