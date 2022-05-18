package com.example.demo.repositories;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

class IntegrationUserRepositoryTest extends AbstractIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        //given
        AppUser savedUser = userRepository.save(new AppUser(null, "mock", "mock", new HashSet<>()));
        //when
        Optional<AppUser> user = userRepository.findByUsername(savedUser.getUsername());
        //then
        then(user.isPresent()).isTrue();
        then(user.get().getUsername()).isEqualTo(savedUser.getUsername());
    }
}