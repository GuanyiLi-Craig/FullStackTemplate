package com.bitforcestudio.usermanager.controller;

import com.bitforcestudio.usermanager.service.UserManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value="/user/signup/{username}/{password}")
    public String signup(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        return userManagerService.signup(username, passwordEncoder.encode(password));
    }

    @GetMapping(value="/user/login/{username}/{password}")
    public Object login(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        System.out.println("User login " + username + " " + password);
        return userManagerService.login(username, passwordEncoder.encode(password));
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