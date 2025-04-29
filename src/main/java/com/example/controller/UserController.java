package com.example.controller;

import com.example.entity.UserDocument;
import com.example.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/quarkus/user")
public class UserController {

    @Inject
    UserService userService;

    @GET
    @Path("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GET
    @Path("")
    public List<UserDocument> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{username}")
    public UserDocument getUserByName(String username) {
        return userService.getUserByName(username);
    }

    @POST
    @Path("")
    public UserDocument createUser(UserDocument userDocument) {
        return userService.createUser(userDocument);
    }

}
