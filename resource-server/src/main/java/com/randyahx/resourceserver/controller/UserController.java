package com.randyahx.resourceserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public String[] getUsers() {
        return new String[] {
                "apple",
                "ramen"
        };
    }
 }
