package com.bitforcestudio.datasetmanager.controller;

import lombok.extern.slf4j.Slf4j;

import com.bitforcestudio.datasetmanager.model.dto.DatasetBasic;
import com.bitforcestudio.datasetmanager.model.dto.DatasetDTO;
import com.bitforcestudio.datasetmanager.service.DatasetManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dataset")
@Slf4j
public class DatasetManagerController {
    
    @Autowired
    DatasetManagerService datasetManagerService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/create")
    public DatasetDTO createDataset(@RequestBody DatasetBasic dataset) {
        log.info(serverPort + " create dataset");
        return datasetManagerService.createDataset(dataset);
    }

    @PostMapping(value = "/update")
    public DatasetDTO updateDataset(@RequestBody DatasetDTO dataset) {
        log.info(serverPort + " update dataset");
        return datasetManagerService.updateDataset(dataset);
    }

    @GetMapping(value = "/datasetid/{datasetid}")
    public DatasetDTO getDatasetByDatasetId(@PathVariable("datasetid") String datasetId) {
        log.info(serverPort + " get dataset by dataset id");

        return datasetManagerService.getDatasetById(datasetId);
    }

    @GetMapping(value = "/projectid/{projectid}")
    public DatasetDTO[] getDatasetByOwnerId(@PathVariable("projectid") String projectId) {
        log.info(serverPort + " get datasets by project id");

        return datasetManagerService.getDatasetsByProjectId(projectId);
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong - dataset manager";
    }
}