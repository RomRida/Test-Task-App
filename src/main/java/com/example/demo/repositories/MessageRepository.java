package com.example.demo.repositories;

import com.example.demo.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserUsername(String username, Pageable pageable);
}
