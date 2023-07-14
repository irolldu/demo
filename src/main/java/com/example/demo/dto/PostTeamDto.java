package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostTeamDto {
    
    private Long id;
    private String memberEmail;
    private Long postId;
    private String name;

}
