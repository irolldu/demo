package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.dto.PostCommentDto;
import com.example.demo.dto.PostCommentListDto;

public interface PostCommentService {
    
    void save(PostCommentDto postCommentDto);
    PostCommentDto findById(Long id);
    Page<PostCommentDto> findByPostId(Long postId, PostCommentListDto postCommentListDto);
    Page<PostCommentDto> findByMemberEmail(String memberEmail, PostCommentListDto postCommentListDto);
    boolean hasPermission(Long id, String email);
    void deleteById(Long id);

}
