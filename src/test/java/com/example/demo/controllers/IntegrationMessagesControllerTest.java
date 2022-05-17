package com.example.demo.controllers;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.AppUser;
import com.example.demo.models.Message;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser
class IntegrationMessagesControllerTest extends AbstractIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void init() {
        AppUser appUser = userRepository.save(new AppUser(1L,
                "user",
                "$2a$07$kqcWYsYZjQJzw4DAMinsmOHdtmE9G5glTC8LmKBJfCy7Y2E3Fl9om",
                new HashSet<>()));
        messageRepository.save(new Message(1L, appUser, "history book"));
    }

    @Test
    void SuccessfulPostCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"user\",\"message\":\"history 1\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk());
    }

    @Test
    void SuccessfulMessageHistoryCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"user\",\"message\":\"history 1\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"user\",\"message\":\"history book\"}]"));
    }

    @Test
    void SuccessfulSaveNewMessageCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"user\",\"message\":\"history book\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk())
                .andExpect(content().json(messageJson));
    }

    @Test
    void unsuccessfulPostCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"usr\",\"message\":\"history book\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isNotFound());
    }

}
