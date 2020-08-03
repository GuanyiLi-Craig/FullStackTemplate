package com.bitforcestudio.datasetmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DatasetBasic {
    private String datasetName;
    private String datasetDetail;
    private String projectId;
}