package com.example.demo.configuration;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.PostCommentDto;
import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostTeamDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostCategoryEntity;
import com.example.demo.entity.PostCommentEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.entity.PostPrizeBenefitEntity;
import com.example.demo.entity.PostTeamEntity;
import com.example.demo.entity.PrizeBenefitEntity;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CompanyTypeRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PrizeBenefitRepository;
import com.example.demo.repository.PrizeTotalRepository;
import com.example.demo.repository.QPostCommentRepository;
import com.example.demo.repository.QPostRepository;

@Configuration
public class ModelMapperConfiguration {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
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
    private QPostCommentRepository qPostCommentRepository;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper
            .typeMap(MemberEntity.class, MemberDto.class);
        modelMapper
            .typeMap(MemberDto.class, MemberEntity.class)
            .addMappings(mapper -> {
                mapper
                    .using(context -> passwordEncoder.encode(String.valueOf(context.getSource())))
                    .map(MemberDto::getPassword, MemberEntity::setPassword);
            });
        modelMapper
            .typeMap(PostDto.class, PostEntity.class)
            .addMappings(mapping -> {
                mapping.using(new Converter<String, MemberEntity>() {

                    @Override
                    public MemberEntity convert(MappingContext<String, MemberEntity> context) {
                        String email = context.getSource();
                        return memberRepository.findByEmail(email);
                    }
                    
                }).map(PostDto::getMemberEmail, PostEntity::setMemberEntity);
            }).setPostConverter(context -> {
                PostDto source = context.getSource();
                PostEntity destination = context.getDestination();
                destination.setPostCategoryEntities(source.getCategoryIds().stream().map(categoryId -> new PostCategoryEntity(null, destination, categoryId)).collect(Collectors.toSet()));
                destination.setPostPrizeBenefitEntities(source.getPrizeBenefitIds().stream().map(prizeBenefitId -> new PostPrizeBenefitEntity(null, destination, prizeBenefitId)).collect(Collectors.toSet()));
                return destination;
            });
        modelMapper
            .typeMap(PostEntity.class, PostDto.class)
            .addMappings(mapping -> {
                mapping
                    .using(new Converter<Set<PostCategoryEntity>, Set<Long>>() {

                        @Override
                        public Set<Long> convert(MappingContext<Set<PostCategoryEntity>, Set<Long>> context) {
                            return context.getSource().stream().map(PostCategoryEntity::getCategoryId).collect(Collectors.toSet());
                        }
                        
                    }).map(PostEntity::getPostCategoryEntities, PostDto::setCategoryIds);
                mapping
                    .using(new Converter<Set<PostPrizeBenefitEntity>, Set<Long>>() {

                        @Override
                        public Set<Long> convert(MappingContext<Set<PostPrizeBenefitEntity>, Set<Long>> context) {
                            return context.getSource().stream().map(PostPrizeBenefitEntity::getPrizeBenefitId).collect(Collectors.toSet());
                        }
                        
                    }).map(PostEntity::getPostPrizeBenefitEntities, PostDto::setPrizeBenefitIds);
            });
        modelMapper
            .typeMap(PostEntity.class, PostViewDto.class)
            .addMappings(mapping -> {
                mapping
                    .using(new Converter<Set<PostCategoryEntity>, Set<String>>() {

                        @Override
                        public Set<String> convert(MappingContext<Set<PostCategoryEntity>, Set<String>> context) {
                            return context.getSource().stream().map(PostCategoryEntity::getCategoryId).map(categoryId -> categoryRepository.findById(categoryId)).map(CategoryEntity::getName).collect(Collectors.toSet());
                        }
                        
                    }).map(PostEntity::getPostCategoryEntities, PostViewDto::setCategoryNames);
                mapping
                    .using(new Converter<Long, String>() {

                        @Override
                        public String convert(MappingContext<Long, String> context) {
                            return companyTypeRepository.findById(context.getSource()).getName();
                        }
                        
                    }).map(PostEntity::getCompanyTypeId, PostViewDto::setCompanyTypeName);
                mapping
                    .using(new Converter<Long, String>() {

                        @Override
                        public String convert(MappingContext<Long, String> context) {
                            return prizeTotalRepository.findById(context.getSource()).getName();
                        }
                        
                    }).map(PostEntity::getPrizeTotalId, PostViewDto::setPrizeTotalName);
                mapping
                    .using(new Converter<Set<PostPrizeBenefitEntity>, Set<String>>() {

                        @Override
                        public Set<String> convert(MappingContext<Set<PostPrizeBenefitEntity>, Set<String>> context) {
                            return context.getSource().stream().map(PostPrizeBenefitEntity::getPrizeBenefitId).map(prizeBenefitId -> prizeBenefitRepository.findById(prizeBenefitId)).map(PrizeBenefitEntity::getName).collect(Collectors.toSet());
                        }
                        
                    }).map(PostEntity::getPostPrizeBenefitEntities, PostViewDto::setPrizeBenefitNames);
            });
        modelMapper
            .typeMap(PostCommentDto.class, PostCommentEntity.class)
            .addMappings(mapping -> {
                mapping
                    .using(new Converter<String, MemberEntity>() {

                        @Override
                        public MemberEntity convert(MappingContext<String, MemberEntity> context) {
                            String email = context.getSource();
                            return memberRepository.findByEmail(email);
                        }

                    }).map(PostCommentDto::getMemberEmail, PostCommentEntity::setMemberEntity);
                mapping
                    .using(new Converter<Long, PostEntity>() {

                        @Override
                        public PostEntity convert(MappingContext<Long, PostEntity> context) {
                            Long postId = context.getSource();
                            return qPostRepository.findById(postId);
                        }
                        
                    }).map(PostCommentDto::getPostId, PostCommentEntity::setPostEntity);
            }).setPostConverter(context -> {
                PostCommentDto source = context.getSource();
                PostCommentEntity destination = context.getDestination();
                if (source.getCommentId() == null) {
                    destination.setPostCommentEntity(destination);
                } else {
                    destination.setPostCommentEntity(qPostCommentRepository.findById(source.getCommentId()));
                }
                return destination;
            });
        modelMapper
            .typeMap(PostCommentEntity.class, PostCommentDto.class)
            .addMappings(mapping -> {
                mapping
                    .using(new Converter<MemberEntity, String>() {

                        @Override
                        public String convert(MappingContext<MemberEntity, String> context) {
                            MemberEntity memberEntity = context.getSource();
                            if (memberEntity != null) {
                                return memberEntity.getEmail();
                            } else {
                                return null;
                            }
                        }
                        
                    }).map(PostCommentEntity::getMemberEntity, PostCommentDto::setMemberEmail);
                mapping
                    .using(new Converter<MemberEntity, String>() {

                        @Override
                        public String convert(MappingContext<MemberEntity, String> context) {
                            MemberEntity memberEntity = context.getSource();
                            if (memberEntity != null) {
                                return memberEntity.getName();
                            } else {
                                return null;
                            }
                        }
                        
                    }).map(PostCommentEntity::getMemberEntity, PostCommentDto::setName);
                mapping
                    .using(new Converter<PostCommentEntity, Long>() {

                        @Override
                        public Long convert(MappingContext<PostCommentEntity, Long> context) {
                            return context.getSource().getId();
                        }
                        
                    }).map(PostCommentEntity::getPostCommentEntity, PostCommentDto::setCommentId);
            });
        modelMapper
            .typeMap(PostTeamDto.class, PostTeamEntity.class)
            .addMappings(mapping -> {
                mapping
                    .using(new Converter<String, MemberEntity>() {

                        @Override
                        public MemberEntity convert(MappingContext<String, MemberEntity> context) {
                            String email = context.getSource();
                            return memberRepository.findByEmail(email);
                        }

                    }).map(PostTeamDto::getMemberEmail, PostTeamEntity::setMemberEntity);
                mapping
                    .using(new Converter<Long, PostEntity>() {

                        @Override
                        public PostEntity convert(MappingContext<Long, PostEntity> context) {
                            Long postId = context.getSource();
                            return qPostRepository.findById(postId);
                        }
                        
                    }).map(PostTeamDto::getPostId, PostTeamEntity::setPostEntity);
            });
        modelMapper
            .typeMap(PostTeamEntity.class, PostTeamDto.class)
            .addMappings(mapping -> {
                mapping
                    .using(new Converter<MemberEntity, String>() {

                        @Override
                        public String convert(MappingContext<MemberEntity, String> context) {
                            MemberEntity memberEntity = context.getSource();
                            if (memberEntity != null) {
                                return memberEntity.getName();
                            } else {
                                return null;
                            }
                        }
                        
                    }).map(PostTeamEntity::getMemberEntity, PostTeamDto::setName);
            });
        return modelMapper;
    }

}
