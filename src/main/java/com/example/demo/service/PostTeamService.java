package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostTeamDto;
import com.example.demo.dto.PostViewDto;

public interface PostTeamService {
    
    void save(PostTeamDto postTeamDto);
    List<PostTeamDto> findByPostId(Long postId);
    boolean exists(PostTeamDto postTeamDto);
    void delete(PostTeamDto postTeamDto);
    Page<PostViewDto> findByMemberEmail(String memberEmail, PostListDto postListDto);

}
