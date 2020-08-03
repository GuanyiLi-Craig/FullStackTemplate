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
    private String baseURL;
    private String variablesRange;
    private String projectId;
    private Date modifiedTime;

    public DatasetDTO(String datasetId, String datasetName, String datasetDetail, String baseURL, String variablesRange, String projectiId) {
        this(datasetId, datasetName, datasetDetail, baseURL, variablesRange, projectiId, new Date(System.currentTimeMillis()));
    }
}