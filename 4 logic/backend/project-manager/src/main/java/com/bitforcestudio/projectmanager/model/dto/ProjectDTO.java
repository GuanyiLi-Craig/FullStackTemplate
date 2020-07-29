package com.bitforcestudio.projectmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String projectId;
    private String projectName;
    private String projectDetail;
    private Integer ownerId;
    private List<Integer> viewersId;
    private Boolean isArchived;
    private Date modifiedTime;

    public ProjectDTO(String projectId, String projectName, String projectDetail, Integer ownerId) {
        this(projectId, projectName, projectDetail, ownerId, new ArrayList<Integer>(), false, new Date(System.currentTimeMillis()));
    }

    public ProjectDTO(String projectId, String projectName, String projectDetail, Integer ownerId,
                      List<Integer> viewersId, Boolean isArchived) {
        this(projectId, projectName, projectDetail, ownerId, viewersId, isArchived, 
             new Date(System.currentTimeMillis()));
    }
}