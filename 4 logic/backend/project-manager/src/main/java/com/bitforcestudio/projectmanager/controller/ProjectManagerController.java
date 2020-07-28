package com.bitforcestudio.projectmanager.controller;

import com.bitforcestudio.projectmanager.model.dto.ProjectDTO;
import com.bitforcestudio.projectmanager.service.ProjectManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController(value = "/project")
@Slf4j
public class ProjectManagerController {

    @Autowired
    private ProjectManagerService projectManagerService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/create")
    public ProjectDTO createProject(@RequestBody ProjectDTO project) {
        log.info(serverPort + " create project");
        return projectManagerService.createProject(project);
    }

    @PostMapping(value = "/update")
    public ProjectDTO updateProject(@RequestBody ProjectDTO project) {
        log.info(serverPort + "update project");
        return projectManagerService.updateProject(project);
    }

    @GetMapping(value = "/projectid/{projetid}")
    public ProjectDTO getProjectByProjectId(@PathVariable("projectid") String projectId) {
        log.info(serverPort + " get project by project id");

        return projectManagerService.getProjectById(projectId);
    }

    @GetMapping(value = "/ownerid/{ownerid}")
    public ProjectDTO[] getProjectByOwnerId(@PathVariable("ownerid") Integer ownerId) {
        log.info(serverPort + " get projects by owner id");

        return projectManagerService.getProjectsByUser(ownerId);
    }
}