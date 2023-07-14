package com.example.demo.dto;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostViewDto {
    
    private Long id;
    private String title;
    private String website;
    private Set<String> categoryNames;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String company;
    private String companyTypeName;
    private String company2;
    private String company3;
    private Long prizeTop;
    private String prizeTotalName;
    private Set<String> prizeBenefitNames;
    private String description;
    private String image;
    private Long viewCount;
    private Long teamCount;
    private Long commentCount;

}
