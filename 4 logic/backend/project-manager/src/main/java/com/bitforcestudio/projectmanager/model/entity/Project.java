package com.bitforcestudio.projectmanager.model.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Project implements Serializable {

    private static final long serialVersionUID = 31874109846893656L;

    private Integer id;
    private String projectId;
    private String projectName;
    private String projectDetail;
    private Integer ownerId;
    private Integer[] viewersId;
    private Boolean isArchived;
    private Date modifiedTime;

    public Project(String projectId, String projectName, String projectDetail, Integer ownerId) {
        this(-1, projectId, projectName, projectDetail, ownerId, new Integer[0], false, new Date(System.currentTimeMillis()));
    }
}