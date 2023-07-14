package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    
    @GetMapping(path = "/")
    public String home() {
        return "home";
    }

    @GetMapping(path = "/login")
    public String login(HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("url", httpServletRequest.getHeader("Referer"));
        return "login";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication, Model model) {
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        model.addAttribute("url", "/");
        model.addAttribute("message", "로그아웃했습니다.");
        return "message";
    }

}
