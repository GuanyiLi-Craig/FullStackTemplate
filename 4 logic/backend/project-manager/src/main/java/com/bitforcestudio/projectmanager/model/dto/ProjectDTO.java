package com.bitforcestudio.projectmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ProjectDTO {
    private String projectId;
    private String projectName;
    private String projectDetail;
    private Integer ownerId;
    private Integer[] viewersId;
    private Boolean isArchived;
    private Date modifiedTime;

    public ProjectDTO(String projectId, String projectName, String projectDetail, Integer ownerId) {
        this(projectId, projectName, projectDetail, ownerId, new Integer[0], false, new Date(System.currentTimeMillis()));
    }
}