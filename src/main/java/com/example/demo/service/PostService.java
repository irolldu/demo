package com.example.demo.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostViewDto;

public interface PostService {
    
    void save(PostDto postDto);
    PostDto findByIdAsPostDto(Long id);
    PostViewDto findByIdAsPostViewDto(Long id);
    Page<PostViewDto> findAll(PostListDto postListDto);
    Page<PostViewDto> findAll(String email, PostListDto postListDto);
    boolean hasPermission(Long id, String email);
    void deleteById(Long id);
    void incrementViewCount(Long id);
    void updateTeamCount(Long id);
    void updateCommentCount(Long id);
    Map<String, Object> findEntities();

}
