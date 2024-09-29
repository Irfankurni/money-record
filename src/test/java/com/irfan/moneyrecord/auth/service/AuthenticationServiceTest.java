package com.irfan.moneyrecord.auth.service;

import com.irfan.moneyrecord.auth.dto.AuthenticationRequest;
import com.irfan.moneyrecord.auth.dto.AuthenticationResponse;
import com.irfan.moneyrecord.auth.dto.RegisterRequest;
import com.irfan.moneyrecord.config.JwtService;
import com.irfan.moneyrecord.exception.InvalidLoginException;
import com.irfan.moneyrecord.token.model.Token;
import com.irfan.moneyrecord.token.repository.TokenRepository;
import com.irfan.moneyrecord.user.model.Role;
import com.irfan.moneyrecord.user.model.User;
import com.irfan.moneyrecord.user.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletOutputStream outputStream;


    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        user = User.builder()
                .id("1")
                .fullName("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    public void testRegister() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("1", response.getData().getId());
        assertEquals("Test User", response.getData().getFullName());

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void testAuthenticateSuccess() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("1", response.getData().getId());
        assertEquals("Test User", response.getData().getFullName());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void testAuthenticateFailure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException());

        assertThrows(InvalidLoginException.class, () -> {
            authenticationService.authenticate(authenticationRequest);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testRevokeAllUserTokens() {
        Token token1 = Token.builder().token("token1").expired(false).revoked(false).build();
        Token token2 = Token.builder().token("token2").expired(false).revoked(false).build();

        when(tokenRepository.findAllValidTokenByUser(anyString())).thenReturn(java.util.Arrays.asList(token1, token2));

        authenticationService.revokeAllUserTokens(user);

        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
        assertTrue(token2.isExpired());
        assertTrue(token2.isRevoked());

        verify(tokenRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testRefreshTokenSuccess() throws IOException {
        String refreshToken = "validRefreshToken";
        String accessToken = "newAccessToken";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(accessToken);
        when(response.getOutputStream()).thenReturn(outputStream);

        authenticationService.refreshToken(request, response);

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).isTokenValid(refreshToken, user);
        verify(jwtService, times(1)).generateToken(user);
        verify(tokenRepository, times(1)).save(any(Token.class));
        verify(response, times(1)).getOutputStream();
    }


    @Test
    public void testRefreshTokenInvalidHeader() throws IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        authenticationService.refreshToken(request, response);

        verify(jwtService, never()).extractUsername(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenRepository, never()).save(any(Token.class));
        verify(response, never()).getOutputStream();
    }

    @Test
    public void testRefreshTokenInvalidToken() throws IOException {
        String refreshToken = "invalidRefreshToken";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(false);

        authenticationService.refreshToken(request, response);

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).isTokenValid(refreshToken, user);
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenRepository, never()).save(any(Token.class));
        verify(response, never()).getOutputStream();
    }
}
