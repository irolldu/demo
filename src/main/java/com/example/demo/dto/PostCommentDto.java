package com.example.demo.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentDto {
    
    private Long id;
    private String memberEmail;
    private Long postId;
    private Long commentId;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date timeStamp;
    @NotBlank(message = "댓글이 공백일 수 없습니다.")
    private String comment;
    private boolean permission;

}
