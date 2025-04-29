package com.example.service;

import com.example.entity.UserDocument;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    private final String DB_NAME = "test";
    private final String COLLECTION_NAME = "users";

    @Inject
    MongoClient mongoClient;

    public UserDocument createUser(UserDocument userDocument) {
        String username = userDocument.getUsername();
        String email = userDocument.getEmail();
        // Logic to create a user in the database
        // For example, using MongoDB client to insert a user document
        var database = mongoClient.getDatabase(DB_NAME);
        var collection = database.getCollection(COLLECTION_NAME);

        var data = new org.bson.Document("username", username)
                .append("email", email);
        var result = collection.insertOne(data);
        if (result.wasAcknowledged()) {
            return new UserDocument(data.getObjectId("_id").toString(), username, email);
        } else {
            throw new RuntimeException("Failed to create user");
        }
    }

    public UserDocument getUserByName(String username) {
        // Logic to retrieve a user from the database
        // For example, using MongoDB client to find a user document
        var database = mongoClient.getDatabase(DB_NAME);
        var collection = database.getCollection(COLLECTION_NAME);

        var query = new org.bson.Document("username", username);
        var userDocument = collection.find(query).first();

        if (userDocument != null) {
            return new UserDocument(userDocument.getObjectId("_id").toString(),
                    userDocument.getString("username"),
                    userDocument.getString("email"));
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<UserDocument> getAllUsers() {
        // Logic to retrieve all users from the database
        // For example, using MongoDB client to find all user documents
        var database = mongoClient.getDatabase(DB_NAME);
        var collection = database.getCollection(COLLECTION_NAME);

        var userDocuments = collection.find().into(new ArrayList<>());

        return userDocuments.stream()
                .map(doc -> new UserDocument(doc.getObjectId("_id").toString(),
                        doc.getString("username"),
                        doc.getString("email")))
                .collect(Collectors.toList());
    }

}
