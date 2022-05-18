package com.example.demo.services;

import com.example.demo.config.test.AbstractIT;
import com.example.demo.models.AppUser;
import com.example.demo.models.Message;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@Transactional
class IntegrationMessageServiceTest extends AbstractIT {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindUserMessages(){
        //given
        AppUser savedUser = new AppUser(null, "mock", "mock", new HashSet<>());
        userRepository.save(savedUser);
        Message savedMessage = new Message(null, savedUser,"mock message");
        messageRepository.save(savedMessage);
        Pageable pageable = PageRequest.of(0, 1);
        //when
        List<Message> messages = messageService.findUserMessages(savedUser.getUsername(), pageable);
        //then
        then(messages.size()).isEqualTo(1);
        then(messages.get(0).getMessageText()).isEqualTo(savedMessage.getMessageText());
    }
}