package com.example.mongodbsearch.controller;

import com.example.mongodbsearch.service.SearchService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search/withSpanish")
    public List<Document> searchInIndex01(@RequestParam String query) {
        return searchService.searchWithSpanish(query, "productDetails");
    }

    @GetMapping("/search/withCustomAnalyzersOnProductID")
    public List<Document> searchInIndex02(@RequestParam String query) {
        return searchService.withCustomAnalyzersOnProductID(query, "productDetails");
    }

    @GetMapping("/search/withCustomAnalyzerOnProductLink")
    public List<Document> searchInIndex_02(@RequestParam String query) {
        return searchService.withCustomAnalyzersOnProductLink(query, "productDetails");
    }

    @GetMapping("/search/withLookups")
    public List<Document> searchInIndex04(@RequestParam String query) {
        return searchService.searchWithLookups(query, "productReviews");
    }
}
