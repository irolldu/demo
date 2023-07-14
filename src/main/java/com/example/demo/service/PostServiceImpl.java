package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CompanyTypeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.PrizeBenefitRepository;
import com.example.demo.repository.PrizeTotalRepository;
import com.example.demo.repository.QPostCommentRepository;
import com.example.demo.repository.QPostRepository;
import com.example.demo.repository.QPostTeamRepository;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private QPostRepository qPostRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CompanyTypeRepository companyTypeRepository;
    @Autowired
    private PrizeTotalRepository prizeTotalRepository;
    @Autowired
    private PrizeBenefitRepository prizeBenefitRepository;
    @Autowired
    private QPostTeamRepository qPostTeamRepository;
    @Autowired
    private QPostCommentRepository qPostCommentRepository;

    public PostEntity findById(Long id) {
        return qPostRepository.findById(id);
    }

    @Override
    public void save(PostDto postDto) {
        PostEntity postEntity = null;
        if (postDto.getId() == null) {
            postEntity = modelMapper.map(postDto, PostEntity.class);
        } else {
            postEntity = qPostRepository.findById(postDto.getId());
            modelMapper.map(postDto, postEntity);
        }
        postRepository.save(postEntity);
    }

    @Override
    public PostDto findByIdAsPostDto(Long id) {
        PostEntity postEntity = findById(id);
        return modelMapper.map(postEntity, PostDto.class);
    }

    @Override
    public PostViewDto findByIdAsPostViewDto(Long id) {
        PostEntity postEntity = findById(id);
        return modelMapper.map(postEntity, PostViewDto.class);
    }

    @Override
    public Page<PostViewDto> findAll(PostListDto postListDto) {
        Pageable pageable = PageRequest.of(postListDto.getPage() - 1, postListDto.getPageSize(), postListDto.getSort());
        List<PostViewDto> content = qPostRepository.findAll(postListDto, pageable);
        long total = qPostRepository.count(postListDto);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<PostViewDto> findAll(String email, PostListDto postListDto) {
        Pageable pageable = PageRequest.of(postListDto.getPage() - 1, postListDto.getPageSize(), postListDto.getSort());
        List<PostViewDto> content = qPostRepository.findAll(email, postListDto, pageable);
        long total = qPostRepository.count(email, postListDto);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public boolean hasPermission(Long id, String email) {
        return qPostRepository.existsByIdAndMemberEmail(id, email);
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public void incrementViewCount(Long id) {
        PostEntity postEntity = findById(id);
        postEntity.incrementViewCount();
        postRepository.save(postEntity);
    }

    @Override
    public void updateTeamCount(Long id) {
        PostEntity postEntity = findById(id);
        postEntity.setTeamCount(qPostTeamRepository.countByPostId(id));
        postRepository.save(postEntity);
    }

    @Override
    public void updateCommentCount(Long id) {
        PostEntity postEntity = findById(id);
        postEntity.setCommentCount(qPostCommentRepository.countByPostId(id));
        postRepository.save(postEntity);
    }

    @Override
    public Map<String, Object> findEntities() {
        Map<String, Object> entities = new HashMap<>();
        entities.put("categoryEntities", categoryRepository.findAll());
        entities.put("companyTypeEntities", companyTypeRepository.findAll());
        entities.put("prizeTotalEntities", prizeTotalRepository.findAll());
        entities.put("prizeBenefitEntities", prizeBenefitRepository.findAll());
        return entities;
    }

}
