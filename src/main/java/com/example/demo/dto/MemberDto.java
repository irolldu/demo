package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String company;
    private boolean validated;

}
