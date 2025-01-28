package com.irfan.moneyrecord.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irfan.moneyrecord.auth.dto.AuthenticationRequest;
import com.irfan.moneyrecord.auth.dto.AuthenticationResponse;
import com.irfan.moneyrecord.auth.dto.RegisterRequest;
import com.irfan.moneyrecord.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken("testToken");

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(authenticationResponse)));

        verify(authenticationService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    public void testAuthenticate() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken("testToken");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(authenticationResponse)));

        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    public void testRefreshToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doNothing().when(authenticationService).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

        mockMvc.perform(post("/api/v1/auth/refresh-token"))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
