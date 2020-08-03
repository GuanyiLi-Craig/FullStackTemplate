package com.bitforcestudio.datasetmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bitforcestudio.datasetmanager.mapper.DatasetMapper;
import com.bitforcestudio.datasetmanager.model.dto.DatasetBasic;
import com.bitforcestudio.datasetmanager.model.dto.DatasetDTO;
import com.bitforcestudio.datasetmanager.model.entity.Dataset;
import com.bitforcestudio.datasetmanager.service.DatasetManagerService;

@Service
@Slf4j
public class DatasetManagerServiceImpl implements DatasetManagerService {
    
    @Autowired
    private DatasetMapper datasetMapper;

    @Override
    public DatasetDTO createDataset(DatasetBasic dataset) {
        // get unique dataset id
        String datasetId = UUID.randomUUID().toString();
        while(datasetMapper.getDatasetByDatasetId(datasetId) != null) {
            datasetId = UUID.randomUUID().toString();
        }
        log.info("new dataset id : " + datasetId + " with size " + datasetId.length());
        DatasetDTO datasetDTO = new DatasetDTO(datasetId,
            dataset.getDatasetName(),
            dataset.getDatasetDetail(),
            dataset.getProjectId());
        datasetMapper.createNewDataset(getDataset(datasetDTO));

        return getDatasetDTO(datasetMapper.getDatasetByDatasetId(datasetId));
    }

    @Override
    public DatasetDTO getDatasetById(String datasetId) {
        return getDatasetDTO(datasetMapper.getDatasetByDatasetId(datasetId));
    }

    @Override
    public DatasetDTO[] getDatasetsByProjectId(String projectId) {
        List<Dataset> datasets = datasetMapper.getDatasetsByProjectId(projectId);
        DatasetDTO[] datasetDTOs = new DatasetDTO[datasets.size()];
        datasets.stream()
                .map(dataset -> getDatasetDTO(dataset))
                .collect(Collectors.toList()).toArray(datasetDTOs);
        
        return datasetDTOs;
    }

    @Override
    public DatasetDTO updateDataset(DatasetDTO datasetDto) {
        datasetMapper.updateDataset(getDataset(datasetDto));

        Dataset dataset = datasetMapper.getDatasetByDatasetId(datasetDto.getDatasetId());

        return getDatasetDTO(dataset);
    }

    @Override
    public boolean deleteDatasetById(String datasetId) {
        return false;
    }

    @Override
    public boolean deleteAllDatasetsByProjectId(String projectId) {
        return false;
    }

    private Dataset getDataset(DatasetDTO datasetDto) {
        return new Dataset(-1,
            datasetDto.getDatasetId(),
            datasetDto.getDatasetName(),
            datasetDto.getDatasetDetail(),
            datasetDto.getProjectId(),
            datasetDto.getModifiedTime());
    }

    private DatasetDTO getDatasetDTO(Dataset dataset) {
        return new DatasetDTO(dataset.getDatasetId(),
            dataset.getDatasetName(),
            dataset.getDatasetDetail(),
            dataset.getProjectId(),
            dataset.getModifiedTime());
    }
}