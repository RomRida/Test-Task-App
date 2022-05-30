package com.example.demo.services;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.Message;
import com.example.demo.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class IntegrationMessageServiceCacheTest extends AbstractIT {
    @Autowired
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void init() {
        //clear cash after every method. This integration test needs it to work correctly
        for (String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }

    @Test
    void getUserByUsernameMultipleRequestCacheTest() {
        //given
        String username = "user";
        Pageable pageable = PageRequest.of(0, 1);
        given(messageRepository.findByUserUsername(username, pageable))
                .willReturn(new ArrayList<>());
        //when
        messageService.findUserMessages(username, pageable);
        messageService.findUserMessages(username, pageable);
        messageService.findUserMessages(username, pageable);
        messageService.findUserMessages(username, pageable);
        //then
        then(messageRepository)
                .should(times(1))
                .findByUserUsername(username, pageable);
    }

    @Test
    void testCacheFlashAfterNewMessageSaved() {
        //given
        String username = "user";
        Pageable pageable = PageRequest.of(0, 1);
        given(messageRepository.findByUserUsername(username, pageable))
                .willReturn(new ArrayList<>());
        Message newSavedMessage = new Message(null,
                null,
                "mocked");
        given(messageRepository.save(newSavedMessage))
                .willReturn(newSavedMessage);
        //when
        messageService.findUserMessages(username, pageable);
        messageService.findUserMessages(username, pageable);

        messageService.saveNewMessage(newSavedMessage);

        messageService.findUserMessages(username, pageable);
        messageService.findUserMessages(username, pageable);
        //then
        then(messageRepository)
                .should(times(2))
                .findByUserUsername(username, pageable);
    }
}
