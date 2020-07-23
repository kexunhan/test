package com.example.test;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class TestApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    void contextLoads() {

        long douYinHao = mongoTemplate.getCollection("BLOGGER-DETAILS").countDocuments(Filters.eq("douYinHao", "抖音号：keke0202"));

        System.out.println(douYinHao);
    }

    @Test
    void haha(){
        // 130
        long l = mongoTemplate.getCollection("BLOGGER-DETAILS").countDocuments();
        System.out.println(l);
    }

}
