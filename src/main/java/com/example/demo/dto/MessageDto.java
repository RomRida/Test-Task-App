package com.example.demo.dto;

import com.example.demo.models.AppUser;
import com.example.demo.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    private String name;
    private  String message;

    public Message convertDtoToEntity(AppUser user){
        return new Message(null, user, this.message);
    }

    public static MessageDto convertEntityToDTO(Message message){
        return new MessageDto(message.getUser().getUsername(), message.getMessageText());
    }
}
