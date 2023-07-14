package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_team")
@Getter
@Setter
public class PostTeamEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (MEMBERID) REFERENCES MEMBER ON DELETE SET NULL"))
    private MemberEntity memberEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (POSTID) REFERENCES POST ON DELETE CASCADE"))
    private PostEntity postEntity;

}
