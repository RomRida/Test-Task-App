package com.example.demo.config.security;

import com.example.demo.exceptions.UserDoesNotExistException;
import com.example.demo.models.AppUser;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            AppUser user = userService.findByUsername(username);
            return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
        }catch (UserDoesNotExistException e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
