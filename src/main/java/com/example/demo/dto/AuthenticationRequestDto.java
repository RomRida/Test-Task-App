package com.example.demo.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String name;
    private String password;
}
