package com.example.demo.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostViewDto;
import com.example.demo.entity.PostEntity;
import com.example.demo.entity.QMemberEntity;
import com.example.demo.entity.QPostCategoryEntity;
import com.example.demo.entity.QPostEntity;
import com.example.demo.entity.QPostPrizeBenefitEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class QPostRepositoryImpl extends QuerydslRepositorySupport implements QPostRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private final QPostEntity qPostEntity;
    private final QPostCategoryEntity qPostCategoryEntity;
    private final QPostPrizeBenefitEntity qPostPrizeBenefitEntity;
    private final QMemberEntity qMemberEntity;

    public QPostRepositoryImpl() {
        super(PostEntity.class);
        qPostEntity = QPostEntity.postEntity;
        qPostCategoryEntity = QPostCategoryEntity.postCategoryEntity;
        qPostPrizeBenefitEntity = QPostPrizeBenefitEntity.postPrizeBenefitEntity;
        qMemberEntity = QMemberEntity.memberEntity;
    }
    
    @Override
    public PostEntity findById(Long id) {
        return from(qPostEntity)
        .leftJoin(qPostEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostEntity.postCategoryEntities, qPostCategoryEntity)
        .fetchJoin()
        .leftJoin(qPostEntity.postPrizeBenefitEntities, qPostPrizeBenefitEntity)
        .fetchJoin()
        .where(qPostEntity.id.eq(id))
        .distinct()
        .fetchOne();
    }

    @Override
    public List<PostViewDto> findAll(PostListDto postListDto, Pageable pageable) {
        return jpaQueryFactory
        .select(Projections.bean(PostViewDto.class, qPostEntity.id, qPostEntity.title, qPostEntity.endDate, qPostEntity.company, qPostEntity.image, qPostEntity.viewCount, qPostEntity.teamCount, qPostEntity.commentCount))
        .from(qPostEntity)
        .leftJoin(qPostEntity.postCategoryEntities, qPostCategoryEntity)
        .leftJoin(qPostEntity.postPrizeBenefitEntities, qPostPrizeBenefitEntity)
        .where(whereClause(postListDto))
        .orderBy(orderByClause(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct()
        .fetch();
    }

    @Override
    public long count(PostListDto postListDto) {
        return from(qPostEntity)
        .leftJoin(qPostEntity.postCategoryEntities, qPostCategoryEntity)
        .fetchJoin()
        .leftJoin(qPostEntity.postPrizeBenefitEntities, qPostPrizeBenefitEntity)
        .fetchJoin()
        .where(whereClause(postListDto))
        .distinct()
        .fetchCount();
    }

    @Override
    public List<PostViewDto> findAll(String email, PostListDto postListDto, Pageable pageable) {
        return jpaQueryFactory
        .select(Projections.bean(PostViewDto.class, qPostEntity.id, qPostEntity.title, qPostEntity.endDate, qPostEntity.company, qPostEntity.image, qPostEntity.viewCount, qPostEntity.teamCount, qPostEntity.commentCount))
        .from(qPostEntity)
        .leftJoin(qPostEntity.memberEntity, qMemberEntity)
        .leftJoin(qPostEntity.postCategoryEntities, qPostCategoryEntity)
        .leftJoin(qPostEntity.postPrizeBenefitEntities, qPostPrizeBenefitEntity)
        .where(qMemberEntity.email.eq(email), whereClause(postListDto))
        .orderBy(orderByClause(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct()
        .fetch();
    }
    
    @Override
    public long count(String email, PostListDto postListDto) {
        return from(qPostEntity)
        .leftJoin(qPostEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .leftJoin(qPostEntity.postCategoryEntities, qPostCategoryEntity)
        .fetchJoin()
        .leftJoin(qPostEntity.postPrizeBenefitEntities, qPostPrizeBenefitEntity)
        .fetchJoin()
        .where(qMemberEntity.email.eq(email), whereClause(postListDto))
        .distinct()
        .fetchCount();
    }

    @Override
    public boolean existsByIdAndMemberEmail(Long id, String email) {
        return from(qPostEntity)
        .leftJoin(qPostEntity.memberEntity, qMemberEntity)
        .fetchJoin()
        .where(qPostEntity.id.eq(id), qMemberEntity.email.eq(email))
        .distinct()
        .fetchCount() > 0;
    }

    private Predicate whereClause(PostListDto postListDto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!postListDto.getCategoryIds().isEmpty()) {
            booleanBuilder.and(qPostCategoryEntity.categoryId.in(postListDto.getCategoryIds()));
        }
        if (!postListDto.getCompanyTypeIds().isEmpty()) {
            booleanBuilder.and(qPostEntity.companyTypeId.in(postListDto.getCompanyTypeIds()));
        }
        if (!postListDto.getPrizeTotalIds().isEmpty()) {
            booleanBuilder.and(qPostEntity.prizeTotalId.in(postListDto.getPrizeTotalIds()));
        }
        if (!postListDto.getPrizeBenefitIds().isEmpty()) {
            booleanBuilder.and(qPostPrizeBenefitEntity.prizeBenefitId.in(postListDto.getPrizeBenefitIds()));
        }
        if (!postListDto.getKeyword().isEmpty()) {
            BooleanBuilder nestedBooleanBuilder = new BooleanBuilder();
            nestedBooleanBuilder.or(qPostEntity.title.contains(postListDto.getKeyword()));
            nestedBooleanBuilder.or(qPostEntity.company.contains(postListDto.getKeyword()));
            nestedBooleanBuilder.or(qPostEntity.company2.contains(postListDto.getKeyword()));
            nestedBooleanBuilder.or(qPostEntity.company3.contains(postListDto.getKeyword()));
            nestedBooleanBuilder.or(qPostEntity.description.contains(postListDto.getKeyword()));
            booleanBuilder.and(nestedBooleanBuilder);
        }
        booleanBuilder.and(qPostEntity.endDate.after(LocalDate.now()));
        return booleanBuilder;
    }

    private OrderSpecifier<?>[] orderByClause(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Order order : sort) {
            ComparableExpressionBase<?> expression = null;
            switch(order.getProperty()) {
                case "endDate":
                    expression = qPostEntity.endDate;
                    break;
                case "viewCount":
                    expression = qPostEntity.viewCount;
                    break;
                case "commentCount":
                    expression = qPostEntity.commentCount;
                    break;
            }
            if (expression == null) {
                continue;
            }
            if (order.isAscending()) {
                orderSpecifiers.add(expression.asc());
            } else {
                orderSpecifiers.add(expression.desc());
            }
        }
        orderSpecifiers.add(qPostEntity.id.desc());
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

}
