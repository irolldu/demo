package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    
    MemberEntity findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);

}
