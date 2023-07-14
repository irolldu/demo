package com.example.demo.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    
    private Long id;
    private String memberEmail;
    private String title;
    private String website;
    private Set<Long> categoryIds = new HashSet<>();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String company;
    private Long companyTypeId;
    private String company2;
    private String company3;
    private Long prizeTop;
    private Long prizeTotalId;
    private Set<Long> prizeBenefitIds = new HashSet<>();
    private String description;
    private String image;
    private MultipartFile file;
    private Long viewCount;
    private Long teamCount;
    private Long commentCount;
    private boolean validated;

}
