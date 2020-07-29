package com.bitforcestudio.projectmanager.model.dto;

public class ProjectBasic {
    private String projectName;
    private String projectDetail;
    private Integer ownerId;

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDetail() {
        return projectDetail;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDetail(String projectDetail) {
        this.projectDetail = projectDetail;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}