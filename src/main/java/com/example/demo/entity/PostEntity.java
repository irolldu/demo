package com.example.demo.entity;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class PostEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (MEMBERID) REFERENCES MEMBER (ID) ON DELETE SET NULL"))
    private MemberEntity memberEntity;
    @Column(nullable = false)
    private String title;
    private String website;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postEntity")
    private Set<PostCategoryEntity> postCategoryEntities;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate startDate;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private Long companyTypeId;
    private String company2;
    private String company3;
    private Long prizeTop;
    private Long prizeTotalId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postEntity")
    private Set<PostPrizeBenefitEntity> postPrizeBenefitEntities;
    @Lob
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String image;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long viewCount;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long teamCount;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long commentCount;

    public void incrementViewCount() {
        viewCount++;
    }

    public void decrementViewCount() {
        viewCount--;
    }

    public void incrementTeamCount() {
        teamCount++;
    }

    public void decrementTeamCount() {
        teamCount--;
    }

    public void incrementCommentCount() {
        commentCount++;
    }

    public void decrementCommentCount() {
        commentCount--;
    }

}
