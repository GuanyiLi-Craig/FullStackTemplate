package com.bitforcestudio.webscraper.controller;

import com.bitforcestudio.webscraper.service.WebScraperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScraperController {
    
    @Autowired
    private WebScraperService webScraperService;

    @GetMapping(value="/webscraper/create/{baseurl}/{args}")
    public String createScraperJob(@PathVariable("baseurl") String baseUrl, @PathVariable("args") String args) {
        return webScraperService.createScraperJob(baseUrl, args);
    }
}