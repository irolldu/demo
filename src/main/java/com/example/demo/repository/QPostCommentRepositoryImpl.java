package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.PostCommentDto;
import com.example.demo.entity.PostCommentEntity;
import com.example.demo.entity.QMemberEntity;
import com.example.demo.entity.QPostCommentEntity;
import com.example.demo.entity.QPostEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class QPostCommentRepositoryImpl extends QuerydslRepositorySupport implements QPostCommentRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private final QPostCommentEntity qPostCommentEntity;
    private final QPostCommentEntity parentPostCommentEntity;
    private final QPostEntity qPostEntity;
    private final QMemberEntity qMemberEntity;

    public QPostCommentRepositoryImpl() {
        super(QPostCommentEntity.class);
        qPostCommentEntity = new QPostCommentEntity("postCommentEntity");
        parentPostCommentEntity = new QPostCommentEntity("parentPostCommentEntity");
        qPostEntity = QPostEntity.postEntity;
        qMemberEntity = QMemberEntity.memberEntity;
    }

    @Override
    public PostCommentEntity findById(Long id) {
        return from(qPostCommentEntity)
        .leftJoin(qPostCommentEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostCommentEntity.postEntity, qPostEntity)
        .fetchJoin()
        .leftJoin(qPostCommentEntity.postCommentEntity, parentPostCommentEntity)
        .fetchJoin()
        .where(qPostCommentEntity.id.eq(id))
        .distinct()
        .fetchOne();
    }

    @Override
    public List<PostCommentDto> findByPostId(Long postId, Pageable pageable) {
        return jpaQueryFactory
        .select(Projections.bean(PostCommentDto.class, qPostCommentEntity.id, parentPostCommentEntity.id.as("commentId"), qMemberEntity.email.as("memberEmail"), qPostEntity.id.as("postId"), qMemberEntity.name, qPostCommentEntity.timeStamp, qPostCommentEntity.comment))
        .from(qPostCommentEntity)
        .leftJoin(qPostCommentEntity.memberEntity, qMemberEntity)
        .leftJoin(qPostCommentEntity.postEntity, qPostEntity)
        .leftJoin(qPostCommentEntity.postCommentEntity, parentPostCommentEntity)
        .where(qPostEntity.id.eq(postId))
        .orderBy(parentPostCommentEntity.id.desc(), qPostCommentEntity.id.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct()
        .fetch();
    }

    @Override
    public long countByPostId(Long postId) {
        return from(qPostCommentEntity)
        .leftJoin(qPostCommentEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostCommentEntity.postEntity, qPostEntity)
        .fetchJoin()
        .leftJoin(qPostCommentEntity.postCommentEntity, parentPostCommentEntity)
        .fetchJoin()
        .where(qPostEntity.id.eq(postId))
        .distinct()
        .fetchCount();
    }

    @Override
    public List<PostCommentDto> findByMemberEmail(String email, Pageable pageable) {
        return jpaQueryFactory
        .select(Projections.bean(PostCommentDto.class, qPostCommentEntity.id, parentPostCommentEntity.id.as("commentId"), qMemberEntity.email.as("memberEmail"), qPostEntity.id.as("postId"), qMemberEntity.name, qPostCommentEntity.timeStamp, qPostCommentEntity.comment))
        .from(qPostCommentEntity)
        .leftJoin(qPostCommentEntity.memberEntity, qMemberEntity)
        .leftJoin(qPostCommentEntity.postEntity, qPostEntity)
        .leftJoin(qPostCommentEntity.postCommentEntity, parentPostCommentEntity)
        .where(qMemberEntity.email.eq(email))
        .orderBy(parentPostCommentEntity.id.desc(), qPostCommentEntity.id.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct()
        .fetch();
    }

    @Override
    public long countByMemberEmail(String email) {
        return from(qPostCommentEntity)
        .leftJoin(qPostCommentEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostCommentEntity.postEntity, qPostEntity)
        .fetchJoin()
        .leftJoin(qPostCommentEntity.postCommentEntity, parentPostCommentEntity)
        .fetchJoin()
        .where(qMemberEntity.email.eq(email))
        .distinct()
        .fetchCount();
    }

    @Override
    public boolean existsByIdAndMemberEmail(Long id, String email) {
        return from(qPostCommentEntity)
        .leftJoin(qPostCommentEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .where(qPostCommentEntity.id.eq(id), qMemberEntity.email.eq(email))
        .distinct()
        .fetchCount() > 0;
    }

}
