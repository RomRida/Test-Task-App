package com.example.demo.repositories;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

import static org.assertj.core.api.BDDAssertions.then;

class IntegrationUserRepositoryTest extends AbstractIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        //given
        AppUser savedUser = userRepository.save(new AppUser(1L, "user", "password", new HashSet<>()));
        //when
        AppUser user = userRepository.findByUsername("user").orElse(null);
        //then
        then(user.getUserId()).isNotNull();
        then(user.getUsername()).isEqualTo(savedUser.getUsername());
    }
}
