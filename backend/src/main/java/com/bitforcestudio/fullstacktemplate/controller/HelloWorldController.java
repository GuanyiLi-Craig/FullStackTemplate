package com.bitforcestudio.fullstacktemplate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    
    @GetMapping("/helloworld")
    public String HelloWorld() {
        System.out.println("get hello world");
        return "Hello World";
    }
}