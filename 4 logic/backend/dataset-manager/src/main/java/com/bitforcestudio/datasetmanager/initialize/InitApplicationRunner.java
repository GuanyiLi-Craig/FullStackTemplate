package com.bitforcestudio.datasetmanager.initialize;

import com.bitforcestudio.datasetmanager.mapper.DatasetMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitApplicationRunner implements ApplicationRunner {

    @Autowired
    private DatasetMapper datasetMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Create dataset table if not exist.");
        datasetMapper.createDatasetTable();
    }
    
}