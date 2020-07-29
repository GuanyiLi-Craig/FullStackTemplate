package com.bitforcestudio.projectmanager.service;

import com.bitforcestudio.projectmanager.model.dto.ProjectBasic;
import com.bitforcestudio.projectmanager.model.dto.ProjectDTO;

public interface ProjectManagerService {
    
    // create
    public ProjectDTO createProject(ProjectBasic project);

    // get
    public ProjectDTO getProjectById(String projectId);
    public ProjectDTO[] getProjectsByUser(Integer userId);

    // update
    public ProjectDTO updateProject(ProjectDTO project);

    // delete
    public boolean deleteProjectById(String projectId);
    public boolean deleteAllProjectsByUser(String userId);
}