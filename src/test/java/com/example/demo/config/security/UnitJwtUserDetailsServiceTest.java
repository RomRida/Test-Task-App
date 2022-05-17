package com.example.demo.config.security;

import com.example.demo.models.AppUser;
import com.example.demo.services.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UnitJwtUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Before
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulLoadUserByUsername() {
        //given
        AppUser mockedUser = new AppUser(1L, "user", "password", new HashSet<>());
        given(userService.findByUsername(mockedUser.getUsername())).willReturn(mockedUser);
        //when
        UserDetails user = jwtUserDetailsService.loadUserByUsername(mockedUser.getUsername());
        //then
        assertThat(user.getUsername()).isEqualTo(mockedUser.getUsername());
    }

    @Test
    void testFailedLoadUserByUsername() {
        //given
        AppUser mockedUser = new AppUser(1L, "user", "password", new HashSet<>());
        given(userService.findByUsername(any())).willThrow(UsernameNotFoundException.class);
        //when
        Throwable throwable = catchThrowable(() -> jwtUserDetailsService.loadUserByUsername(mockedUser.getUsername()));
        //then
        then(throwable).isInstanceOf(UsernameNotFoundException.class);
    }
}