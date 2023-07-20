package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_comment")
@Getter
@Setter
public class PostCommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (MEMBERID) REFERENCES MEMBER (ID) ON DELETE SET NULL"))
    private MemberEntity memberEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (POSTID) REFERENCES POST (ID) ON DELETE CASCADE"))
    private PostEntity postEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCommentId", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (POSTCOMMENTID) REFERENCES POST_COMMENT (ID) ON DELETE CASCADE"))
    private PostCommentEntity postCommentEntity;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timeStamp;
    @Column(nullable = false)
    private String comment;

}
