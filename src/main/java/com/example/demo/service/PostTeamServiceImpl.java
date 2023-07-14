package com.example.demo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostTeamDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.PostTeamEntity;
import com.example.demo.repository.PostTeamRepository;
import com.example.demo.repository.QPostTeamRepository;

@Service
@Transactional
public class PostTeamServiceImpl implements PostTeamService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostTeamRepository postTeamRepository;
    @Autowired
    private QPostTeamRepository qPostTeamRepository;

    @Override
    public void save(PostTeamDto postTeamDto) {
        PostTeamEntity postTeamEntity = modelMapper.map(postTeamDto, PostTeamEntity.class);
        postTeamRepository.save(postTeamEntity);
    }

    @Override
    public List<PostTeamDto> findByPostId(Long postId) {
        return qPostTeamRepository.findByPostId(postId);
    }

    @Override
    public boolean exists(PostTeamDto postTeamDto) {
        return qPostTeamRepository.existsByPostIdAndMemberEmail(postTeamDto.getPostId(), postTeamDto.getMemberEmail());
    }

    @Override
    public void delete(PostTeamDto postTeamDto) {
        PostTeamEntity postTeamEntity = qPostTeamRepository.findByPostIdAndMemberEmail(postTeamDto.getPostId(), postTeamDto.getMemberEmail());
        postTeamRepository.delete(postTeamEntity);
    }

    @Override
    public Page<PostViewDto> findByMemberEmail(String memberEmail, PostListDto postListDto) {
        Pageable pageable = PageRequest.of(postListDto.getPage() - 1, postListDto.getPageSize(), postListDto.getSort());
        List<PostViewDto> content = qPostTeamRepository.findByMemberEmail(memberEmail, pageable);
        long total = qPostTeamRepository.countByMemberEmail(memberEmail);
        return new PageImpl<>(content, pageable, total);
    }

}
