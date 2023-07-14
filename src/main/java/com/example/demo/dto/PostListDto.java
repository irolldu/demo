package com.example.demo.dto;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostListDto {
    
    private int tabId = 1;
    private String keyword = "";
    private Set<Long> categoryIds = new HashSet<>();
    private Set<Long> companyTypeIds = new HashSet<>();
    private Set<Long> prizeTotalIds = new HashSet<>();
    private Set<Long> prizeBenefitIds = new HashSet<>();
    private int sortId = 1;
    private int page = 1;
    private int pageSize = 20;
    private int blockSize = 10;

    public Sort getSort() {
        Sort sort = null;
        switch (sortId) {
            case 2:
                sort = Sort.by(Order.asc("endDate"));
                break;
            case 3:
                sort = Sort.by(Order.desc("viewCount"));
                break;
            case 4:
                sort = Sort.by(Order.desc("teamCount"));
                break;
            case 5:
                sort = Sort.by(Order.desc("commentCount"));
                break;
            default:
                sort = Sort.by(Order.desc("id"));
                break;
        }
        return sort;
    }

}
