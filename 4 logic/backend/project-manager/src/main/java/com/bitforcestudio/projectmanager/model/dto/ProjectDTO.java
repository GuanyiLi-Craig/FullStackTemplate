package com.bitforcestudio.projectmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectDTO {
    private String projectId;
    private String projectName;
    private String projectDetail;
}