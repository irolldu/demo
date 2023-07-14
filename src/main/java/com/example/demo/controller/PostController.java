package com.example.demo.controller;

import java.security.Principal;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.PostCommentDto;
import com.example.demo.dto.PostCommentListDto;
import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostListDto;
import com.example.demo.dto.PostTeamDto;
import com.example.demo.service.FileService;
import com.example.demo.service.PostCommentService;
import com.example.demo.service.PostService;
import com.example.demo.service.PostTeamService;
import com.example.demo.validator.PostAddValidator;
import com.example.demo.validator.PostEditValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class PostController {
    
    @Autowired
    private PostAddValidator postAddValidator;
    @Autowired
    private PostEditValidator postEditValidator;
    @Autowired
    private PostService postService;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private PostTeamService postTeamService;
    @Autowired
    private FileService fileService;

    @GetMapping(path = "/post/list")
    public String list(Model model, PostListDto postListDto) {
        model.addAllAttributes(postService.findEntities());
        model.addAttribute("postListDto", postListDto);
        model.addAttribute("postList", postService.findAll(postListDto));
        return "post/list";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping(path = "/post/add")
    public String add(Model model, PostDto postDto, BindingResult bindingResult) {
        postAddValidator.validate(postDto, bindingResult);
        model.addAttribute("postDto", postDto);
        model.addAttribute("bindingResult", bindingResult);
        model.addAllAttributes(postService.findEntities());
        return "post/add";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/post/add")
    public String add(Principal principal, RedirectAttributes redirectAttributes, Model model, PostDto postDto, BindingResult bindingResult) throws Exception {
        postDto.setImage(fileService.save(postDto.getFile()));
        postDto.setValidated(true);
        postAddValidator.validate(postDto, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("postDto", postDto);
            return "redirect:/post/add";
        } else {
            postDto.setMemberEmail(principal.getName());
            postService.save(postDto);
            model.addAttribute("url", "/manage/post");
            model.addAttribute("message", "포스트를 추가했습니다.");
            return "message";
        }
    }

    @GetMapping(path = "/post/view/{id}")
    public String view(Principal principal, Model model, @PathVariable(name = "id") Long id, PostCommentListDto postCommentListDto) {
        postService.incrementViewCount(id);
        
        Page<PostCommentDto> postCommentList = postCommentService.findByPostId(id, postCommentListDto);
        if (principal != null) {
            for (PostCommentDto postCommentDto : postCommentList.getContent()) {
                if (postCommentDto.getMemberEmail() == null) {
                    continue;
                } else if (postCommentDto.getMemberEmail().equals(principal.getName())) {
                    postCommentDto.setPermission(true);
                }
            }
        }

        model.addAttribute("postViewDto", postService.findByIdAsPostViewDto(id));
        model.addAttribute("postCommentList", postCommentList);
        model.addAttribute("postTeamDtos", postTeamService.findByPostId(id));
        return "post/view";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping(path = "/post/edit/{id}")
    public String edit(Model model, PostDto postDto, BindingResult bindingResult) {
        if (!postDto.isValidated()) {
            postDto = postService.findByIdAsPostDto(postDto.getId());
        }
        postEditValidator.validate(postDto, bindingResult);
        model.addAllAttributes(postService.findEntities());
        model.addAttribute("postDto", postDto);
        model.addAttribute("bindingResult", bindingResult);
        return "post/edit";
    }
    
    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/post/edit/{id}")
    public String edit(RedirectAttributes redirectAttributes, Model model, PostDto postDto, BindingResult bindingResult) throws Exception {
        if (!postDto.getFile().isEmpty()) {
            postDto.setImage(fileService.save(postDto.getFile()));
        }
        postDto.setValidated(true);
        postEditValidator.validate(postDto, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("postDto", postDto);
            return "redirect:/post/edit/" + postDto.getId();
        } else {
            postService.save(postDto);
            model.addAttribute("url", "/manage/post");
            model.addAttribute("message", "포스트를 수정했습니다.");
            return "message";
        }
        
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/post/delete/{id}")
    public String delete(Principal principal, Model model, @PathVariable(name = "id") Long id) {
        if (postService.hasPermission(id, principal.getName())) {
            postService.deleteById(id);
            model.addAttribute("url", "/manage/post");
            model.addAttribute("message", "포스트를 삭제했습니다.");
        }
        return "message";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/post/comment/add")
    public String addComment(HttpServletRequest httpServletRequest, Principal principal, Model model, @Valid PostCommentDto postCommentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("url", httpServletRequest.getHeader("Referer"));
            model.addAttribute("message", bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        } else {
            postCommentDto.setMemberEmail(principal.getName());
            postCommentDto.setTimeStamp(new Date());
            postCommentService.save(postCommentDto);
            postService.updateCommentCount(postCommentDto.getPostId());
            
            model.addAttribute("url", httpServletRequest.getHeader("Referer"));
            model.addAttribute("message", "댓글을 추가했습니다.");
        }
        return "message";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/post/comment/delete/{id}")
    public String deleteComment(HttpServletRequest httpServletRequest, Principal principal, Model model, @PathVariable(name = "id") Long id) {
        PostCommentDto postCommentDto = postCommentService.findById(id);
        if (postCommentDto.getMemberEmail().equals(principal.getName())) {
            postCommentService.deleteById(id);
            postService.updateCommentCount(postCommentDto.getPostId());
            model.addAttribute("url", httpServletRequest.getHeader("Referer"));
            model.addAttribute("message", "댓글을 삭제했습니다.");
        }
        return "message";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/post/team")
    public String toggleTeam(HttpServletRequest httpServletRequest, Principal principal, Model model, PostTeamDto postTeamDto) {
        postTeamDto.setMemberEmail(principal.getName());
        if (postTeamService.exists(postTeamDto)) {
            postTeamService.delete(postTeamDto);
            postService.updateTeamCount(postTeamDto.getPostId());
            model.addAttribute("url", httpServletRequest.getHeader("Referer"));
            model.addAttribute("message", "취소했습니다.");
        } else {
            postTeamService.save(postTeamDto);
            postService.updateTeamCount(postTeamDto.getPostId());
            model.addAttribute("url", httpServletRequest.getHeader("Referer"));
            model.addAttribute("message", "지원했습니다.");
        }
        return "message";
    }

}
