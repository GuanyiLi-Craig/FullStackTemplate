package com.bitforcestudio.usermanager.initialize;

import com.bitforcestudio.usermanager.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Create user table if not exist.");
        userMapper.createUserTable();
    }
    
}
