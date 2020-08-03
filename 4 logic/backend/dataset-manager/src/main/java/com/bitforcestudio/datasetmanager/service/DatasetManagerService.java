package com.bitforcestudio.datasetmanager.service;

import com.bitforcestudio.datasetmanager.model.dto.DatasetBasic;
import com.bitforcestudio.datasetmanager.model.dto.DatasetDTO;

public interface DatasetManagerService {

    // create
    public DatasetDTO createDataset(DatasetBasic dataset);

    // get
    public DatasetDTO getDatasetById(String datasetId);
    public DatasetDTO[] getDatasetsByProjectId(String projectId);

    // update
    public DatasetDTO updateDataset(DatasetDTO dataset);

    // delete
    public boolean deleteDatasetById(String datasetId);
    public boolean deleteAllDatasetsByProjectId(String projectId);
}