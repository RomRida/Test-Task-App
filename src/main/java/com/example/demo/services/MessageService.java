package com.example.demo.services;

import com.example.demo.models.Message;
import com.example.demo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveNewMessage(Message message) {
        return messageRepository.save(message);
    }

    @Cacheable("messages")
    public List<Message> findUserMessages(String username, Pageable pageable) {
        return messageRepository.findByUserUsername(username, pageable);
    }
}
