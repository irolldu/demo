package com.example.demo.service;

import com.example.demo.dto.MemberDto;

public interface MemberService {
    
    void insert(MemberDto memberDto);
    void update(MemberDto memberDto);
    MemberDto findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);

}
