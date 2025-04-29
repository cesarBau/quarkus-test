package com.example.service;

import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.time.Instant;

@ApplicationScoped
public class InfoService {

    private final String DB_NAME = "netcas";
    private final String COLLECTION_NAME = "info-quarkus";

    @Inject
    MongoClient mongoClient;

    public void createInfo(String operation) {
        Instant createdAt = Instant.now();

        var database = mongoClient.getDatabase(DB_NAME);
        var collection = database.getCollection(COLLECTION_NAME);

        var data = new Document("operation", operation)
                .append("createdAt", createdAt);
        var result = collection.insertOne(data);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to create info");
        }
    }

}
