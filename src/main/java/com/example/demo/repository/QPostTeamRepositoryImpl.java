package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.PostTeamDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.PostTeamEntity;
import com.example.demo.entity.QMemberEntity;
import com.example.demo.entity.QPostEntity;
import com.example.demo.entity.QPostTeamEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class QPostTeamRepositoryImpl extends QuerydslRepositorySupport implements QPostTeamRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private final QPostTeamEntity qPostTeamEntity;
    private final QPostEntity qPostEntity;
    private final QMemberEntity qMemberEntity;

    public QPostTeamRepositoryImpl() {
        super(PostTeamEntity.class);
        qPostTeamEntity = QPostTeamEntity.postTeamEntity;
        qPostEntity = QPostEntity.postEntity;
        qMemberEntity = QMemberEntity.memberEntity;
    }
    
    @Override
    public List<PostTeamDto> findByPostId(Long postId) {
        return jpaQueryFactory
        .select(Projections.bean(PostTeamDto.class, qPostTeamEntity.id, qMemberEntity.email.as("memberEmail"), qPostEntity.id.as("postId"), qMemberEntity.name.as("name")))
        .from(qPostTeamEntity)
        .leftJoin(qPostTeamEntity.memberEntity, qMemberEntity)
        .leftJoin(qPostTeamEntity.postEntity, qPostEntity)
        .where(qPostEntity.id.eq(postId))
        .orderBy(qPostTeamEntity.id.desc())
        .fetch();
    }

    @Override
    public long countByPostId(Long postId) {
        return from(qPostTeamEntity)
        .leftJoin(qPostTeamEntity.postEntity, qPostEntity)
        .fetchJoin()
        .where(qPostEntity.id.eq(postId))
        .distinct()
        .fetchCount();
    }

    @Override
    public PostTeamEntity findByPostIdAndMemberEmail(Long postId, String memberEmail) {
        return from(qPostTeamEntity)
        .leftJoin(qPostTeamEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostTeamEntity.postEntity, qPostEntity)
        .fetchJoin()
        .where(qPostEntity.id.eq(postId), qMemberEntity.email.eq(memberEmail))
        .fetchOne();
    }

    @Override
    public boolean existsByPostIdAndMemberEmail(Long postId, String memberEmail) {
        return from(qPostTeamEntity)
        .leftJoin(qPostTeamEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostTeamEntity.postEntity, qPostEntity)
        .fetchJoin()
        .where(qPostEntity.id.eq(postId).and(qMemberEntity.email.eq(memberEmail)))
        .fetchOne() != null;
    }

    @Override
    public List<PostViewDto> findByMemberEmail(String memberEmail, Pageable pageable) {
        return jpaQueryFactory
        .select(Projections.bean(PostViewDto.class, qPostEntity.id, qPostEntity.title, qPostEntity.company, qPostEntity.image, qPostEntity.viewCount, qPostEntity.teamCount, qPostEntity.commentCount, qPostTeamEntity.id.as("postTeamId")))
        .from(qPostTeamEntity)
        .leftJoin(qPostTeamEntity.memberEntity, qMemberEntity)
        .leftJoin(qPostTeamEntity.postEntity, qPostEntity)
        .where(qMemberEntity.email.eq(memberEmail))
        .orderBy(qPostTeamEntity.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct()
        .fetch();
    }

    @Override
    public long countByMemberEmail(String memberEmail) {
        return from(qPostTeamEntity)
        .leftJoin(qPostTeamEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostTeamEntity.postEntity, qPostEntity)
        .fetchJoin()
        .where(qMemberEntity.email.eq(memberEmail))
        .distinct()
        .fetchCount();
    }

}
