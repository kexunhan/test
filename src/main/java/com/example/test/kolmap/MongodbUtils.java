package com.example.test.kolmap;


import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongodbUtils {


    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertOne(String collection, Document document) {
        mongoTemplate.getCollection(collection).insertOne(document);
    }
}
