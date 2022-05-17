package com.example.demo.controllers;

import com.example.demo.config.security.jwt.JwtTokenProvider;
import com.example.demo.dto.AuthenticationRequestDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UnitLoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private LoginController loginController;

    @Before
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulLogin() {
        //given
        AuthenticationRequestDto dto = new AuthenticationRequestDto();
        dto.setName("user");
        dto.setPassword("password");
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getName(), dto.getPassword())))
                .willReturn(any());
        given(jwtTokenProvider.createToken(dto.getName())).willReturn("mockedToken");
        //when
        ResponseEntity<Map<String, String>> response = loginController.login(dto);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testFailedLogin() {
        //given
        AuthenticationRequestDto dto = new AuthenticationRequestDto();
        dto.setName("user");
        dto.setPassword("password");
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getName(), dto.getPassword())))
                .willThrow(new BadCredentialsException(""));
        //when
        Throwable throwable = catchThrowable(() -> loginController.login(dto));
        //then
        then(throwable).isInstanceOf(BadCredentialsException.class);
    }

}