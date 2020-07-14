package com.bitforcestudio.usermanager.service.impl;

import javax.annotation.Resource;

import com.bitforcestudio.usermanager.mapper.UserMapper;
import com.bitforcestudio.usermanager.model.entity.User;
import com.bitforcestudio.usermanager.service.UserManagerService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserManagerServiceImpl implements UserManagerService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String signup(String username, String password) {
        System.out.println(username + "  " + password);
        int result = userMapper.createNewUser(new User(username, password));

        return Integer.toString(result);
    }

    @Override
    public String initialize() {
        Boolean isCreated = userMapper.createUserTable();

        if (isCreated) {
            return "Create user table";
        } else {
            return "User table exists";
        }
    }

    @Override
    public User getUserbyUserName(String username) {
        log.info("get user by name " + username);
        return userMapper.getUserByUserName(username);
    }
    
}