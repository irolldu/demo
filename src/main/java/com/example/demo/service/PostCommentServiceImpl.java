package com.example.demo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PostCommentDto;
import com.example.demo.dto.PostCommentListDto;
import com.example.demo.entity.PostCommentEntity;
import com.example.demo.repository.PostCommentRepository;
import com.example.demo.repository.QPostCommentRepository;

@Service
@Transactional
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private QPostCommentRepository qPostCommentRepository;

    @Override
    public void save(PostCommentDto postCommentDto) {
        PostCommentEntity postCommentEntity = null;
        if (postCommentDto.getId() == null) {
            postCommentEntity = modelMapper.map(postCommentDto, PostCommentEntity.class);
        } else {
            postCommentEntity = qPostCommentRepository.findById(postCommentDto.getId());
            modelMapper.map(postCommentDto, postCommentEntity);
        }
        postCommentRepository.save(postCommentEntity);
    }

    @Override
    public PostCommentDto findById(Long id) {
        return modelMapper.map(qPostCommentRepository.findById(id), PostCommentDto.class);
    }

    @Override
    public Page<PostCommentDto> findByPostId(Long postId, PostCommentListDto postCommentListDto) {
        Pageable pageable = PageRequest.of(postCommentListDto.getPage() - 1, postCommentListDto.getPageSize(), Sort.unsorted());
        List<PostCommentDto> content = qPostCommentRepository.findByPostId(postId, pageable);
        long total = qPostCommentRepository.countByPostId(postId);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<PostCommentDto> findByMemberEmail(String memberEmail, PostCommentListDto postCommentListDto) {
        Pageable pageable = PageRequest.of(postCommentListDto.getPage() - 1, postCommentListDto.getPageSize(), Sort.unsorted());
        List<PostCommentDto> content = qPostCommentRepository.findByMemberEmail(memberEmail, pageable);
        long total = qPostCommentRepository.countByMemberEmail(memberEmail);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public boolean hasPermission(Long id, String email) {
        return qPostCommentRepository.existsByIdAndMemberEmail(id, email);
    }

    @Override
    public void deleteById(Long id) {
        postCommentRepository.deleteById(id);
    }

}
