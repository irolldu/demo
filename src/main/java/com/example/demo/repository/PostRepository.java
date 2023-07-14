package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    
}
