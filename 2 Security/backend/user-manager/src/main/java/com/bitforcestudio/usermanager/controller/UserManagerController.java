package com.bitforcestudio.usermanager.controller;

import com.bitforcestudio.usermanager.entities.LoginForm;
import com.bitforcestudio.usermanager.service.UserManagerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/user/signup/{username}/{password}")
    public String signup(@PathVariable("username") String username, @PathVariable("password") String password) {
        return userManagerService.signup(username, passwordEncoder.encode(password));
    }

    @PostMapping(value = "/login")
    public Object login(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginForm loginForm = objectMapper.readValue(data, LoginForm.class);
        System.out.println("User login " + loginForm);

        return userManagerService.login(loginForm.getUsername(), passwordEncoder.encode(loginForm.getPassword()));
    }
    
    @GetMapping(value="/user/logout/{username}")
    public String logout(@PathVariable("username") String username) {
        return userManagerService.logout(username);
    }

    @GetMapping(value = "/user/init")
    public String initializeDatabase() {
        return serverPort + " ---> " + userManagerService.initialize(); 
    }
}