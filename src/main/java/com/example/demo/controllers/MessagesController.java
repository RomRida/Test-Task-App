package com.example.demo.controllers;

import com.example.demo.dto.MessageDto;
import com.example.demo.models.AppUser;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/messages")
public class MessagesController {
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessagesController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum.replace(",", "."));
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //converts any number to positive int number
    public static int convertToPositiveInt(String strNum) {
        return (int) Math.abs(Double.parseDouble(strNum.replace(",", ".")));
    }

    @PostMapping
    public ResponseEntity saveNewMessage(@RequestBody MessageDto messageDto) {
        AppUser user = userService.findByUsername(messageDto.getName());

        String[] messageSplit = messageDto.getMessage().split(" ");

        //check if needed to return message history or save new message
        if (messageSplit.length == 2 && messageSplit[0].equals("history") && isNumeric(messageSplit[1])) {
            //assigning given value to Pageable
            int amountOfRecords = convertToPositiveInt(messageSplit[1]);
            Pageable pageable = PageRequest.of(0, amountOfRecords);
            //return list of messages that belongs to given user
            return ResponseEntity.ok(messageService
                    .findUserMessages(messageDto.getName(), pageable).stream()
                    .map(MessageDto::convertEntityToDTO)
                    .collect(Collectors.toList()));
        } else {
            //save new message
            return ResponseEntity.ok(MessageDto.convertEntityToDTO(messageService.saveNewMessage(messageDto.convertDtoToEntity(user))));
        }
    }
}
