package com.bitforcestudio.projectmanager.mapper;

import java.util.List;

import com.bitforcestudio.projectmanager.model.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {
    public boolean createProjectTable();

    public int createNewProject(Project project);

    public boolean removeProject(String projectId);

    public int updateProject(Project project);

    public Project getProjectById(@Param("id") Integer id);

    public Project getProjectByProjectId(@Param("projectId") String projectId);

    public List<Project> getProjectsByOwnerId(@Param("ownerId") Integer ownerId);

    public List<Project> getProjectByViewerId(@Param("viewerId") Integer viewerId);
}
