package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentListDto {
    
    private int page = 1;
    private int pageSize = 10;
    private int blockSize = 10;

}
