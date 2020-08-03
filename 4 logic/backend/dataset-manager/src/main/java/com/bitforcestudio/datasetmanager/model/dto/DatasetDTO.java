package com.bitforcestudio.datasetmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetDTO {
    private String datasetId;
    private String datasetName;
    private String datasetDetail;
    private String projectId;
    private Date modifiedTime;

    public DatasetDTO(String datasetId, String datasetName, String datasetDetail, String projectiId) {
        this(datasetId, datasetName, datasetDetail, projectiId, new Date(System.currentTimeMillis()));
    }
}