package com.example.demo.controllers;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.dto.MessageDto;
import com.example.demo.models.AppUser;
import com.example.demo.models.Message;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
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

    @Autowired
    MessagesController messagesController;

    @BeforeEach
    void init() {
        AppUser appUser = userRepository.save(new AppUser(3L,
                "mock",
                "$2a$07$kqcWYsYZjQJzw4DAMinsmOHdtmE9G5glTC8LmKBJfCy7Y2E3Fl9om",
                new HashSet<>()));
        messageRepository.save(new Message(null, appUser, "mock message"));
    }

    @Test
    void SuccessfulPostCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"mock\",\"message\":\"history 1\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk());
    }

    @Test
    void SuccessfulMessageHistoryCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"mock\",\"message\":\"history 1\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"mock\",\"message\":\"mock message\"}]"));
    }

    @Test
    void SuccessfulSaveNewMessageCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"FirstUser\",\"message\":\"new mock message\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk())
                .andExpect(content().json(messageJson));
    }

    @Test
    void SuccessfulSavedMessage() {
        //given
        MessageDto newUserMessageDto = new MessageDto("FirstUser", "new mock message");
        MessageDto userMessageToRetractHistory = new MessageDto("FirstUser", "history 100");
        List<Message> expectedList = messageRepository.findByUserUsername("FirstUser", PageRequest.of(0, 100));
        //when
        ResponseEntity<MessageDto> response = messagesController.saveNewMessage(newUserMessageDto);
        ResponseEntity<List<MessageDto>> historyResponse = messagesController.saveNewMessage(userMessageToRetractHistory);
        //then
        then(response.getBody().getName()).isEqualTo(newUserMessageDto.getName());
        then(response.getBody().getMessage()).isEqualTo(newUserMessageDto.getMessage());

        then(historyResponse.getBody().size()).isEqualTo(expectedList.size() + 1);
        then(historyResponse.getBody().contains(newUserMessageDto)).isTrue();
    }

    @Test
    void unsuccessfulPostCall() throws Exception {
        //given
        String messageJson = "{\"name\":\"usr\",\"message\":\"mock message\"}";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isNotFound());
    }

}
