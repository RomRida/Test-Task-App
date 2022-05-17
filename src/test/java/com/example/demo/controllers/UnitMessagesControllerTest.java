package com.example.demo.controllers;

import com.example.demo.dto.MessageDto;
import com.example.demo.models.AppUser;
import com.example.demo.models.Message;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UnitMessagesControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessagesController messagesController;

    @Before
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulSaveNewMessage() {
        //given
        AppUser mockedUser = new AppUser(1L, "user", "password", new HashSet<>());
        Message mockedMessage = new Message(1L, mockedUser, "mocked");
        MessageDto dto = MessageDto.convertEntityToDTO(mockedMessage);

        given(userService.findByUsername(mockedUser.getUsername())).willReturn(mockedUser);
        given(messageService.saveNewMessage(any())).willReturn(mockedMessage);
        //when
        ResponseEntity response = messagesController.saveNewMessage(dto);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void testSuccessfulGetOfMessageHistory() {
        //given
        AppUser mockedUser = new AppUser(1L, "user", "password", new HashSet<>());
        Message mockedMessage = new Message(1L, mockedUser, "history 10");
        MessageDto dto = new MessageDto("user", "history 10");

        given(userService.findByUsername(mockedUser.getUsername())).willReturn(mockedUser);
        given(messageService.findUserMessages(mockedUser.getUsername(), PageRequest.of(0, 10)))
                .willReturn(List.of(mockedMessage));
        //when
        ResponseEntity response = messagesController.saveNewMessage(dto);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(dto));
    }
}