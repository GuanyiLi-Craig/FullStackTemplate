package com.bitforcestudio.usermanager.controller;

import com.bitforcestudio.usermanager.service.UserManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    @GetMapping(value="/user/login/{username}/{password}")
    public String login(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        return userManagerService.login(username, password);
    }
    
    @GetMapping(value="/user/logout/{username}")
    public String logout(@PathVariable("username") String username) {
        return userManagerService.logout(username);
    }
}