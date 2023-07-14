package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.PostCommentListDto;
import com.example.demo.dto.PostListDto;
import com.example.demo.service.PostCommentService;
import com.example.demo.service.PostService;
import com.example.demo.service.PostTeamService;

@Controller
public class ManageController {
    
    @Autowired
    private PostService postService;
    @Autowired
    private PostTeamService postTeamService;
    @Autowired
    private PostCommentService postCommentService;


    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping(path = "/manage/post")
    public String post(Principal principal, Model model, PostListDto postListDto) {
        model.addAllAttributes(postService.findEntities());
        model.addAttribute("postListDto", postListDto);
        model.addAttribute("postList", postService.findAll(principal.getName(), postListDto));
        return "manage/post";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping(path = "/manage/team")
    public String team(Principal principal, Model model, PostListDto postListDto) {
        model.addAttribute("postListDto", postListDto);
        model.addAttribute("postList", postTeamService.findByMemberEmail(principal.getName(), postListDto));
        return "manage/team";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping(path = "/manage/comment")
    public String comment(Principal principal, Model model, PostCommentListDto postCommentListDto) {
        model.addAttribute("postCommentListDto", postCommentListDto);
        model.addAttribute("postCommentList", postCommentService.findByMemberEmail(principal.getName(), postCommentListDto));
        return "manage/comment";
    }

}
