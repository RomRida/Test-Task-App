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
import org.springframework.cache.CacheManager;
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

    private static final String PRE_SAVED_MESSAGE = "{\"name\":\"mock\",\"message\":\"mock message\"}";
    private static final String HISTORY_LOOK_UP_MESSAGE = "{\"name\":\"mock\",\"message\":\"history 10\"}";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessagesController messagesController;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void init() {
        AppUser appUser = userRepository.save(new AppUser(3L, "mock", "$2a$07$kqcWYsYZjQJzw4DAMinsmOHdtmE9G5glTC8LmKBJfCy7Y2E3Fl9om", new HashSet<>()));
        messageRepository.save(new Message(null, appUser, "mock message"));
        //clear cash after every method. This integration test needs it to work correctly
        for (String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }

    @Test
    void SuccessfulPostCall() throws Exception {
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HISTORY_LOOK_UP_MESSAGE))
                .andExpect(status().isOk());
    }

    @Test
    void SuccessfulMessageHistoryCall() throws Exception {
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HISTORY_LOOK_UP_MESSAGE))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + PRE_SAVED_MESSAGE + "]"));
    }

    @Test
    void SuccessfulSaveNewMessageCall() throws Exception {
        //given
        String newMessage = "{\"name\":\"mock\",\"message\":\"new mock message\"}";
        String historyCheckResponse = "[" + PRE_SAVED_MESSAGE + "," + newMessage + "]";
        //when //then
        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newMessage))
                .andExpect(status().isOk())
                .andExpect(content().json(newMessage));
        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HISTORY_LOOK_UP_MESSAGE))
                .andExpect(status().isOk())
                .andExpect(content().json(historyCheckResponse));
    }

    @Test
    void SuccessfulSavedMessage() {
        //given
        MessageDto newUserMessageDto = new MessageDto("mock", "new mock message");
        MessageDto userMessageToRetractHistory = new MessageDto("mock", "history 100");
        List<Message> expectedList = messageRepository.findByUserUsername("mock", PageRequest.of(0, 100));
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
