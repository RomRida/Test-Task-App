package com.example.demo.controllers;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.AppUser;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class IntegrationLoginControllerTest extends AbstractIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        AppUser savedUser = userRepository.save(new AppUser(1L,
                "user",
                "$2a$07$kqcWYsYZjQJzw4DAMinsmOHdtmE9G5glTC8LmKBJfCy7Y2E3Fl9om",
                new HashSet<>()));
    }

    @Test
    void SuccessfulLoginTest() throws Exception {
        //given
        String loginJson = "{\"name\":\"user\",\"password\":\"password\"}";
        //when //then
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    void unsuccessfulLoginTest() throws Exception {
        //given
        String loginJson = "{\"name\":\"notARealUser\",\"password\":\"notARealPassword\"}";
        //when //then
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void wrongMessageFormatLoginTest() throws Exception {
        //given
        String loginJson = "{\"a\":\"o\",\"y\":\"i\"}";
        //when //then
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isForbidden());
    }
}