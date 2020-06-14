package com.bitforcestudio.usermanager.service;

public interface UserManagerService {
    public String login(String username, String password);

    public String logout(String username);
}