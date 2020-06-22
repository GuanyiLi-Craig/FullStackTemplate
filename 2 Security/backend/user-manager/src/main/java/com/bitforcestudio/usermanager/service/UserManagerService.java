package com.bitforcestudio.usermanager.service;

import com.bitforcestudio.usermanager.entities.UserEntity;

public interface UserManagerService {

    public String signup(String username, String password);

    public UserEntity login(String username, String password);

    public String logout(String username);

    public String initialize();
}