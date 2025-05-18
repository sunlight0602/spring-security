package com.example.demo.controller;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;


@Controller
public class SecurityController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/menu{num}/{id}")
    public String menu(@PathVariable("num") int num, @PathVariable("id") int id) {
        return "menu" + num + "/" + id;
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @GetMapping("/toLogin/error")
    public String toLogin(HttpServletRequest request, Model model) {
        AuthenticationException exception = (AuthenticationException) request
                .getSession()
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            model.addAttribute("msg", "用戶名或密碼錯誤");
        } else if (exception instanceof DisabledException) {
            model.addAttribute("msg", "用戶已被禁用");
        }
        return "login";
    }
}
