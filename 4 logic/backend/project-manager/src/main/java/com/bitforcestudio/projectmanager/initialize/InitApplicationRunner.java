package com.bitforcestudio.projectmanager.initialize;

import com.bitforcestudio.projectmanager.mapper.ProjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitApplicationRunner implements ApplicationRunner {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Create project table if not exist.");
        projectMapper.createProjectTable();
    }
    
}
