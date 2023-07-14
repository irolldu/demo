package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo.dto.PostTeamDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.PostTeamEntity;

public interface QPostTeamRepository {
    
    List<PostTeamDto> findByPostId(Long postId);
    long countByPostId(Long postId);
    PostTeamEntity findByPostIdAndMemberEmail(Long postId, String memberEmail);
    boolean existsByPostIdAndMemberEmail(Long postId, String memberEmail);
    List<PostViewDto> findByMemberEmail(String memberEmail, Pageable pageable);
    long countByMemberEmail(String memberEmail);

}
