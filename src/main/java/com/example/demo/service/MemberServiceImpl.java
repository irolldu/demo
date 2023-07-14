package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void insert(MemberDto memberDto) {
        MemberEntity memberEntity = modelMapper.map(memberDto, MemberEntity.class);
        memberRepository.save(memberEntity);
    }

    @Override
    public void update(MemberDto memberDto) {
        MemberEntity memberEntity = memberRepository.findByEmail(memberDto.getEmail());
        modelMapper.map(memberDto, memberEntity);
        memberRepository.save(memberEntity);
    }

    @Override
    public MemberDto findByEmail(String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        return modelMapper.map(memberEntity, MemberDto.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        memberRepository.deleteByEmail(email);
    }
    
}
