package com.pair.hyorioka.pair.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello";
    }
    
}
