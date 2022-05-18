package com.example.demo.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @Column(name = "message")
    private String messageText;

}
