package com.bitforcestudio.datasetmanager.mapper;

import java.util.List;

import com.bitforcestudio.datasetmanager.model.entity.Dataset;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DatasetMapper {
    public boolean createDatasetTable();

    public int createNewDataset(Dataset dataset);

    public boolean removeDataset(String datasetId);

    public int updateDataset(Dataset dataset);

    public Dataset getDatasetById(@Param("id") Integer id);

    public Dataset getDatasetByDatasetId(@Param("datasetId") String datasetId);

    public List<Dataset> getDatasetsByProjectId(@Param("projectId") String projectId);
}