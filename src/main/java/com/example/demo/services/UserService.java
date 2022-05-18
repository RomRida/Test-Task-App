package com.example.demo.services;

import com.example.demo.exceptions.UserDoesNotExistException;
import com.example.demo.models.AppUser;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable("users")
    public AppUser findByUsername(String username) {
        //if user not found throw custom exception
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistException("User with username: " + username + " does not exist"));
    }
}
