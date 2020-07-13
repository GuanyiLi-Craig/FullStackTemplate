package com.bitforcestudio.usermanager.controller;

import com.bitforcestudio.usermanager.entities.User;
import com.bitforcestudio.usermanager.entities.UserBasic;
import com.bitforcestudio.usermanager.entities.UserInfo;
import com.bitforcestudio.usermanager.service.UserManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/signup")
    public String signup(@RequestBody UserBasic userBasic) {
        log.info(userBasic.getUsername());
        return userManagerService.signup(userBasic.getUsername(), 
                                         passwordEncoder.encode(userBasic.getPassword()));
    }


    @GetMapping(value = "/user/getUserInfo/{username}")
    public Object getUserInfo(@PathVariable("username") String username) {
        log.info("get user info " + username);
        User user = userManagerService.getUserbyUserName(username);
        
        UserInfo userInfo = new UserInfo(user.getUserName(), user.getModifiedTime().toString(), user.getRoles().toString());
        return userInfo;
    }

    @GetMapping(value = "/user/init")
    public String initializeDatabase() {
        return serverPort + " ---> " + userManagerService.initialize(); 
    }
}