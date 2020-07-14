package com.bitforcestudio.usermanager.service.impl;

import com.bitforcestudio.usermanager.mapper.UserMapper;
import com.bitforcestudio.usermanager.model.entity.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserByUserName(username);
        System.out.println(username + "-->" + user);
        UserDetails userDetails = new UserDetailsImpl(user);
        return userDetails;
    }
    
}