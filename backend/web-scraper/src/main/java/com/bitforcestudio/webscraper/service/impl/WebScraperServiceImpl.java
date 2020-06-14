package com.bitforcestudio.webscraper.service.impl;

import com.bitforcestudio.webscraper.service.WebScraperService;
import org.springframework.stereotype.Service;

@Service
public class WebScraperServiceImpl implements WebScraperService{

    @Override
    public String createScraperJob(String baseUrl, String args) {
        return "Scrap " + baseUrl + " with args " + args;
    }
    
}