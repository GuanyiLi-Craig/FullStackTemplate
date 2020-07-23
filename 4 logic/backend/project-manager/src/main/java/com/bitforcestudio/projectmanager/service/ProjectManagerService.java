package com.bitforcestudio.projectmanager.service;

import com.bitforcestudio.projectmanager.model.dto.ProjectDTO;

public interface ProjectManagerService {
    
    // create
    public ProjectDTO createProject(ProjectDTO project);

    // get
    public ProjectDTO getProjectById(String projectId);
    public ProjectDTO getProjectsByUser(String userId);

    // update
    public ProjectDTO updateProject(ProjectDTO project);

    // delete
    public boolean deleteProjectById(String projectId);
    public boolean deleteAllProjectsByUser(String userId);
}