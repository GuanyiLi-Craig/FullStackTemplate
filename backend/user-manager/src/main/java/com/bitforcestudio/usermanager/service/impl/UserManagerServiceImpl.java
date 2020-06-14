package com.bitforcestudio.usermanager.service.impl;

import com.bitforcestudio.usermanager.service.UserManagerService;
import org.springframework.stereotype.Service;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    @Override
    public String login(String username, String password) {
        return username + " login " + password;
    }

    @Override
    public String logout(String username) {
        return username + " logout";
    }
    
}