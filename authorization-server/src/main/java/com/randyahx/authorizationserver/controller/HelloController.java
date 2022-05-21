package com.randyahx.authorizationserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController @RequiredArgsConstructor
public class HelloController {

    @GetMapping(value="/hello")
    public String hello() {
        return "Hello!";
    }
}