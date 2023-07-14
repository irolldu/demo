package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PostCategoryEntity;

public interface PostCategoryRepository extends JpaRepository<PostCategoryEntity, Long> {
    
}
