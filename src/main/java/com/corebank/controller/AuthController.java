package com.corebank.controller;

import com.corebank.model.User;
import com.corebank.service.AuthService;
import com.corebank.service.SessionService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController() {
        authService = new AuthService();
    }


    // ------------------------------ LOGIN ------------------------------
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User loginData) {

        if (loginData == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Login data is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        User user = authService.login(
            loginData.getUsername(),
            loginData.getPassword()
        );

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Invalid username or password.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        String token = SessionService.createSession(user);

        user.setPassword(null);

        String response = """
            {
                "token": "%s",
                "username": "%s",
                "role": "%s"
            }
            """.formatted(
                token,
                user.getUsername(),
                user.getRole()
            );

        return Response
            .ok(response)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }


    // ------------------------------ LOGOUT ------------------------------
    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(
            @HeaderParam("Authorization") String authorization) {

        if (authorization == null ||
            !authorization.startsWith("Bearer ")) {

            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        String token = authorization.substring(7);

        boolean removed = SessionService.removeSession(token);

        if (!removed) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Invalid session.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok("{\"message\":\"Logout completed successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}