package com.bitforcestudio.usermanager.service.impl;

import javax.annotation.Resource;

import com.bitforcestudio.usermanager.dao.UserDao;
import com.bitforcestudio.usermanager.entities.User;
import com.bitforcestudio.usermanager.service.UserManagerService;

import org.springframework.stereotype.Service;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(String username, String password) {
        User user = userDao.getUserByUserName(username);
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public String logout(String username) {
        return username + " logout";
    }

    @Override
    public String signup(String username, String password) {
        int id = userDao.createNewUser(new User(username, password));

        return Integer.toString(id);
    }
    
}