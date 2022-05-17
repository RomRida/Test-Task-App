package com.example.demo.services;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.exceptions.UserDoesNotExistException;
import com.example.demo.models.AppUser;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class IntegrationUserServiceTest extends AbstractIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserName() throws UserDoesNotExistException {
        //given
        AppUser savedUser = userRepository.save(new AppUser(1L, "user", "password", new HashSet<>()));
        //when
        AppUser user = userService.findByUsername("user");
        //then
        then(user.getUserId()).isNotNull();
        then(user.getUsername()).isEqualTo(savedUser.getUsername());
    }

    @Test
    void testErrorUserDoesNotExistError() {
        //given
        String notExistingUser = "iDoNotExist";
        //when
        Throwable throwable = catchThrowable(() ->userService.findByUsername(notExistingUser));
        //then
        then(throwable).isInstanceOf(UserDoesNotExistException.class);
    }


}