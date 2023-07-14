package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PostCommentEntity;

public interface PostCommentRepository extends JpaRepository<PostCommentEntity, Long> {
    
}
