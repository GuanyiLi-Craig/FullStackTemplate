package com.bitforcestudio.datasetmanager.model.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *  baseURL: variables in {}
 *  variablesRange: defined the variables
 *     [a-b] : range of a to b
 *     [a, b, c] : discrete
 *     [a-b/c] : range of a to b step by c
 *     [**], [**] :multi variables 
 */

@Getter
@Setter
@AllArgsConstructor
public class Dataset implements Serializable {

    private static final long serialVersionUID = -4091659623558874185L;

    private Integer id;
    private String datasetId;
    private String datasetName;
    private String datasetDetail;
    private String projectId;
    private Date modifiedTime;

    public Dataset(String datasetId, String datasetName, String datasetDetail, String projectId) {
        this(-1, datasetId, datasetName, datasetDetail, projectId, new Date(System.currentTimeMillis()));
    }
}