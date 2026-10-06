package com.corebank.controller;

import java.util.List;

import com.corebank.model.Client;
import com.corebank.model.User;
import com.corebank.service.ClientService;
import com.corebank.util.AuthUtil;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clients")
public class ClientController {

    @Inject
    private ClientService clientService;


    // ------------------------- LIST CLIENTS -------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listClients(
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (!AuthUtil.isEmployee(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        List<Client> clients = clientService.listClients();

        return Response
            .ok(clients)
            .build();
    }


    // ------------------------- CREATE CLIENT -------------------------
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createClient(
            Client client,
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (!AuthUtil.isEmployee(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        boolean created = clientService.createClient(client);

        if (!created) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Invalid client data.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .status(Response.Status.CREATED)
            .entity(client)
            .build();
    }


    // ------------------------- FIND CLIENT BY ID -------------------------
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findClientById(
            @PathParam("id") int id,
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        // CLIENT can only access their own data
        if (AuthUtil.isClient(user)) {

            if (user.getClientId() == null ||
                user.getClientId() != id) {

                return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Access denied.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }
        }

        // Only CLIENT or EMPLOYEE can access
        else if (!AuthUtil.isEmployee(user)) {

            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        Client client = clientService.findClientID(id);

        if (client == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Client not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok(client)
            .build();
    }


    // ------------------------- UPDATE CLIENT -------------------------
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(
            @PathParam("id") int id,
            Client client,
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (!AuthUtil.isEmployee(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        Client existingClient = clientService.findClientID(id);

        if (existingClient == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Client not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        client.setId(id);

        clientService.updateClient(client);

        return Response
            .ok(client)
            .build();
    }


    // ------------------------- DELETE CLIENT -------------------------
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteClient(
            @PathParam("id") int id,
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (!AuthUtil.isEmployee(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        Client existingClient = clientService.findClientID(id);

        if (existingClient == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Client not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        clientService.deleteClient(id);

        return Response
            .ok("{\"message\":\"Client deleted successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}