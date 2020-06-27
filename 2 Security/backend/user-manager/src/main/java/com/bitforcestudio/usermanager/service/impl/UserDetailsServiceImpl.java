package com.bitforcestudio.usermanager.service.impl;

import com.bitforcestudio.usermanager.dao.UserDao;
import com.bitforcestudio.usermanager.entities.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUserName(username);
        UserDetails userDetails = new UserDetailsImpl(user);
        return userDetails;
    }
    
}