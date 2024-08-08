package com.example.mongodbsearch.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<Document> searchWithSpanish(String query, String sampleProducts) {
        MongoCollection<Document> collection;
        MongoDatabase database = mongoTemplate.getDb();
        collection = database.getCollection(sampleProducts);
        List<Document> pipeline = Arrays.asList(new Document("$search",
                new Document("index", "index01")
                        .append("text",
                                new Document("query", query)
                                        .append("path", "productNameSpanish"))));

        ArrayList<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).into(results);
        return results;
    }

    public List<Document> withCustomAnalyzersOnProductID(String query, String sampleProducts) {
        MongoCollection<Document> collection;
        MongoDatabase database = mongoTemplate.getDb();
        collection = database.getCollection(sampleProducts);
        List<Document> pipeline = Arrays.asList(new Document("$search",
                        new Document("index", "index02")
                                .append("text",
                                        new Document("query", query)
                                                .append("path", "productID"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("productID", 1L)
                                .append("productName", 1L)));

        ArrayList<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).into(results);
        return results;
        }

    public List<Document> withCustomAnalyzersOnProductLink(String query, String sampleProducts) {
        MongoCollection<Document> collection;
        MongoDatabase database = mongoTemplate.getDb();
        collection = database.getCollection(sampleProducts);
        List<Document> pipeline = Arrays.asList(new Document("$search",
                        new Document("index", "index02")
                                .append("text",
                                        new Document("query", query)
                                                .append("path", "productLink"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("productID", 1L)
                                .append("productName", 1L)
                                .append("productLink", 1L)));

        ArrayList<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).into(results);
        return results;
    }


    public List<Document> searchWithLookups(String query, String productReviews) {
        MongoCollection<Document> collection;
        MongoDatabase database = mongoTemplate.getDb();
        collection = database.getCollection(productReviews);
        List<Document> pipeline = Arrays.asList(new Document("$lookup",
                        new Document("from", productReviews)
                                .append("localField", "productID")
                                .append("foreignField", "productID")
                                .append("as", "result")
                                .append("pipeline", Arrays.asList(new Document("$search",
                                        new Document("index", "index03")
                                                .append("text",
                                                        new Document("query", query)
                                                                .append("path", "review")))))),
                new Document("$match",
                        new Document("result",
                                new Document("$ne", Arrays.asList()))));

        ArrayList<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).into(results);
        return results;
    }
}
