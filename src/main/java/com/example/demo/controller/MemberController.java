package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;
import com.example.demo.validator.MemberAddValidator;
import com.example.demo.validator.MemberEditValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberController {
    
    @Autowired
    private MemberAddValidator memberAddValidator;
    @Autowired
    private MemberEditValidator memberEditValidator;
    @Autowired
    private MemberService memberService;

    @PreAuthorize(value = "!isAuthenticated()")
    @GetMapping(path = "/member/add")
    public String add(Model model, MemberDto memberDto, BindingResult bindingResult) {
        memberAddValidator.validate(memberDto, bindingResult);
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("bindingResult", bindingResult);
        return "member/add";
    }

    @PreAuthorize(value = "!isAuthenticated()")
    @PostMapping(path = "/member/add")
    public String add(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Model model, MemberDto memberDto, BindingResult bindingResult) {
        memberDto.setValidated(true);
        memberAddValidator.validate(memberDto, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("memberDto", memberDto);
            return "redirect:/member/add";
        } else {
            memberService.insert(memberDto);
            model.addAttribute("url", "/");
            model.addAttribute("message", "가입했습니다.");
            return "message";
        }
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping(path = "/member/edit")
    public String edit(Principal principal, Model model, MemberDto memberDto, BindingResult bindingResult) {
        if (!memberDto.isValidated()) {
            memberDto = memberService.findByEmail(principal.getName());
        }
        memberEditValidator.validate(memberDto, bindingResult);
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("bindingResult", bindingResult);
        return "member/edit";
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/member/edit")
    public String edit(Principal principal, RedirectAttributes redirectAttributes, Model model, MemberDto memberDto, BindingResult bindingResult) {
        memberDto.setEmail(principal.getName());
        memberDto.setValidated(true);
        memberEditValidator.validate(memberDto, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("memberDto", memberDto);
            return "redirect:/member/edit";
        } else {
            memberService.update(memberDto);
            model.addAttribute("url", "/");
            model.addAttribute("message", "수정했습니다.");
            return "message";
        }
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping(path = "/member/delete")
    public String delete(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication, Model model) {
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        memberService.deleteByEmail(authentication.getName());
        model.addAttribute("url", "/");
        model.addAttribute("message", "탈퇴했습니다.");
        return "message";
    }

}
