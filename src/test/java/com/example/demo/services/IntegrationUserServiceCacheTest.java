package com.example.demo.services;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.AppUser;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class IntegrationUserServiceCacheTest extends AbstractIT {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void getUserByUsernameMultipleRequestCacheTest() {
        //given
        String username = "user";
        given(userRepository.findByUsername(username))
                .willReturn(Optional.of(new AppUser(1L, "user", "password", new HashSet<>())));
        //when
        userService.findByUsername(username);
        userService.findByUsername(username);
        userService.findByUsername(username);
        userService.findByUsername(username);
        //then
        then(userRepository).should(times(1)).findByUsername(username);
    }
}
