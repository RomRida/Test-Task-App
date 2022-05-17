package com.example.demo.services;

import com.example.demo.exceptions.UserDoesNotExistException;
import com.example.demo.models.AppUser;
import com.example.demo.repositories.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UnitUserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Before
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulFindByUsername() {
        //given
        AppUser mockedUser = new AppUser(1L, "mock", "mock", new HashSet<>());
        given(userRepository.findByUsername(mockedUser.getUsername()))
                .willReturn(Optional.of(mockedUser));
        //when
        AppUser newUser = userService.findByUsername("mock");
        //then
        assertThat(newUser).isEqualTo(mockedUser);
    }

    @Test
    void testFailedFindByUsername() {
        //given
        AppUser mockedUser = new AppUser(1L, "mock", "mock", new HashSet<>());
        given(userRepository.findByUsername(mockedUser.getUsername()))
                .willThrow(new UserDoesNotExistException(""));
        //when
        Throwable throwable = catchThrowable(() -> userService.findByUsername("mock"));
        //then
        then(throwable).isInstanceOf(UserDoesNotExistException.class);
    }
}