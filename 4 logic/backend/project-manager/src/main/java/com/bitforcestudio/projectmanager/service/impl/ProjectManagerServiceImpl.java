package com.bitforcestudio.projectmanager.service.impl;

import com.bitforcestudio.projectmanager.mapper.ProjectMapper;
import com.bitforcestudio.projectmanager.model.dto.ProjectDTO;
import com.bitforcestudio.projectmanager.model.entity.Project;
import com.bitforcestudio.projectmanager.service.ProjectManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectManagerServiceImpl implements ProjectManagerService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public ProjectDTO createProject(ProjectDTO project) {
        // get unique project id
        String projectId = UUID.randomUUID().toString();
        while(projectMapper.getProjectByProjectId(projectId) != null) {
            projectId = UUID.randomUUID().toString();
        }
        project.setProjectId(projectId);
        projectMapper.createNewProject(getProject(project));

        return getProjectDTO(projectMapper.getProjectByProjectId(projectId));
    }

    @Override
    public ProjectDTO getProjectById(String projectId) {
        return getProjectDTO(projectMapper.getProjectByProjectId(projectId));
    }

    @Override
    public ProjectDTO[] getProjectsByUser(Integer userId) {
        List<Project> projects = projectMapper.getProjectsByOwnerId(userId);
        ProjectDTO[] projectDTOs = new ProjectDTO[projects.size()];
        projects.stream()
                .map(project -> getProjectDTO(project))
                .collect(Collectors.toList()).toArray(projectDTOs);
        
        return projectDTOs;
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO project) {
        projectMapper.createNewProject(getProject(project));

        return getProjectDTO(projectMapper.getProjectByProjectId(project.getProjectId()));
    }

    @Override
    public boolean deleteProjectById(String projectId) {
        return false;
    }

    @Override
    public boolean deleteAllProjectsByUser(String userId) {
        return false;
    }

    private ProjectDTO getProjectDTO(Project project) {
        if (project == null) return null;
        return new ProjectDTO(project.getProjectId(),
                project.getProjectName(),
                project.getProjectDetail(),
                project.getOwnerId(),
                project.getViewersId(),
                project.getIsArchived(),
                project.getModifiedTime());
    }

    private Project getProject(ProjectDTO projectDTO) {
        return new Project(-1, projectDTO.getProjectId(), 
            projectDTO.getProjectName(), 
            projectDTO.getProjectDetail(), 
            projectDTO.getOwnerId(), 
            projectDTO.getViewersId(), 
            projectDTO.getIsArchived(), 
            projectDTO.getModifiedTime());
    }
}
