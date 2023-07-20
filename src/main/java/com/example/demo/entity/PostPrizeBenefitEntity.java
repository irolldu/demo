package com.example.demo.entity;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_prizeBenefit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPrizeBenefitEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false, foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (POSTID) REFERENCES POST (ID) ON DELETE CASCADE"))
    private PostEntity postEntity;
    @Column(nullable = false)
    private Long prizeBenefitId;

}
