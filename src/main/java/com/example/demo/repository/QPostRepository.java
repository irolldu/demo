package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.PostEntity;

public interface QPostRepository {
    
    PostEntity findById(Long id);
    List<PostViewDto> findAll(PostListDto postListDto, Pageable pageable);
    long count(PostListDto postListDto);
    List<PostViewDto> findAll(String email, PostListDto postListDto, Pageable pageable);
    long count(String email, PostListDto postListDto);
    boolean existsByIdAndMemberEmail(Long id, String email);

}
