package com.example.demo.services;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}
