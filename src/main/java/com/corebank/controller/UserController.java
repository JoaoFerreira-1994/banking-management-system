package com.corebank.controller;

import com.corebank.model.User;
import com.corebank.service.UserService;
import com.corebank.util.AuthUtil;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserController {

    private UserService userService;

    public UserController() {
        userService = new UserService();
    }


    // ------------------------------ CREATE ------------------------------

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(
            User user,
            @HeaderParam("Authorization") String authorization) {

        User authenticatedUser =
            AuthUtil.getAuthenticatedUser(authorization);

        if (authenticatedUser == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (!AuthUtil.isEmployee(authenticatedUser)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        boolean created = userService.createUser(user);

        if (!created) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"User could not be created.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        // Do not return the password/hash
        user.setPassword(null);

        return Response
            .status(Response.Status.CREATED)
            .entity(user)
            .build();
    }
}