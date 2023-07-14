package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo.dto.PostCommentDto;
import com.example.demo.entity.PostCommentEntity;

public interface QPostCommentRepository {
    
    PostCommentEntity findById(Long id);
    List<PostCommentDto> findByPostId(Long postId, Pageable pageable);
    long countByPostId(Long postId);
    List<PostCommentDto> findByMemberEmail(String email, Pageable pageable);
    long countByMemberEmail(String email);
    boolean existsByIdAndMemberEmail(Long id, String email);

}
