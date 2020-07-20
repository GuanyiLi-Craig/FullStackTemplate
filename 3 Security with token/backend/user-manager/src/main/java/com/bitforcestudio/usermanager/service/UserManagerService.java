package com.bitforcestudio.usermanager.service;

import com.bitforcestudio.usermanager.model.entity.User;

public interface UserManagerService {

    public String signup(String username, String password);

    public User getUserbyUserName(String username);
}