package com.example.demo.services;

import com.example.demo.models.AppUser;
import com.example.demo.models.Message;
import com.example.demo.repositories.MessageRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UnitMessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageService messageService;

    @Before
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulSaveNewMessage() {
        //given
        AppUser mockedUser = new AppUser(1L, "mock", "mock", new HashSet<>());
        Message mockedMessage = new Message(1L, mockedUser, "This is mocked message");
        given(messageRepository.save(any(Message.class))).willReturn(mockedMessage);
        //when
        Message newMessage = messageService.saveNewMessage(mockedMessage);
        //then
        assertThat(newMessage.getUser()).isEqualTo(mockedUser);
        assertThat(newMessage).isEqualTo(mockedMessage);
    }

    @Test
    void testSuccessFindUserMessages() {
        //given
        AppUser mockedUser = new AppUser(1L, "mock", "mock", new HashSet<>());
        Message mockedMessage = new Message(1L, mockedUser, "This is mocked message");
        Pageable pageable = PageRequest.of(0, 1);
        given(messageRepository.findByUserUsername(mockedUser.getUsername(), pageable))
                .willReturn(List.of(mockedMessage));
        //when
        List<Message> newMessages = messageService
                .findUserMessages(mockedUser.getUsername(), pageable);
        //then
        assertThat(newMessages.size()).isEqualTo(1);
        assertThat(newMessages.contains(mockedMessage)).isTrue();
    }


}