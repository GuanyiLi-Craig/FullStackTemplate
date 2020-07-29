package com.bitforcestudio.projectmanager.service.impl;

import com.bitforcestudio.projectmanager.mapper.ProjectMapper;
import com.bitforcestudio.projectmanager.model.dto.ProjectBasic;
import com.bitforcestudio.projectmanager.model.dto.ProjectDTO;
import com.bitforcestudio.projectmanager.model.entity.Project;
import com.bitforcestudio.projectmanager.service.ProjectManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectManagerServiceImpl implements ProjectManagerService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public ProjectDTO createProject(ProjectBasic project) {
        // get unique project id
        String projectId = UUID.randomUUID().toString();
        while(projectMapper.getProjectByProjectId(projectId) != null) {
            projectId = UUID.randomUUID().toString();
        }
        log.info("new project id : " + projectId + " with size " + projectId.length());
        ProjectDTO projectDTO = new ProjectDTO(projectId,
            project.getProjectName(),
            project.getProjectDetail(),
            project.getOwnerId());
        projectMapper.createNewProject(getProject(projectDTO));

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
        projectMapper.updateProject(getProject(project));

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
        List<Integer> viewersId = project.getViewersId().length() == 0 ? 
            new ArrayList<>() : Arrays.asList(project.getViewersId().split(","))
                .stream()
                .map(viewerId -> Integer.parseInt(viewerId))
                .collect(Collectors.toList()); 
        return new ProjectDTO(project.getProjectId(),
                project.getProjectName(),
                project.getProjectDetail(),
                project.getOwnerId(),
                viewersId,
                project.getIsArchived(),
                project.getModifiedTime());
    }

    private Project getProject(ProjectDTO projectDTO) {
        String viewersIdStr = projectDTO.getViewersId().stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        return new Project(-1, projectDTO.getProjectId(), 
            projectDTO.getProjectName(), 
            projectDTO.getProjectDetail(), 
            projectDTO.getOwnerId(), 
            viewersIdStr, 
            projectDTO.getIsArchived(), 
            projectDTO.getModifiedTime());
    }
}
