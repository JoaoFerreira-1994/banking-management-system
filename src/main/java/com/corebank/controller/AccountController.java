package com.corebank.controller;

import java.util.List;

import com.corebank.model.Account;
import com.corebank.model.User;
import com.corebank.service.AccountService;
import com.corebank.util.AuthUtil;

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

@Path("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController() {
        accountService = new AccountService();
    }


    // ------------------------------ LIST ------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAccounts(
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

        List<Account> accounts = accountService.listAccounts();

        return Response
            .ok(accounts)
            .build();
    }


    // ------------------------------ FIND BY ID ------------------------------
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAccountById(
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

        Account account = accountService.findAccountID(id);

        if (account == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        // CLIENT can only access their own account
        if (AuthUtil.isClient(user)) {

            if (user.getClientId() == null ||
                user.getClientId() != account.getClientId()) {

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

        return Response
            .ok(account)
            .build();
    }


    // ------------------------------ CREATE ------------------------------
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(
            Account account,
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

        boolean created = accountService.createAccount(account);

        if (!created) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Invalid account data or client not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .status(Response.Status.CREATED)
            .entity(account)
            .build();
    }


    // ------------------------------ UPDATE ------------------------------
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccount(
            @PathParam("id") int id,
            Account account,
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

        Account existingAccount = accountService.findAccountID(id);

        if (existingAccount == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        account.setId(id);

        accountService.updateAccount(account);

        Account updatedAccount = accountService.findAccountID(id);

        return Response
            .ok(updatedAccount)
            .build();
    }


    // ------------------------------ DELETE ------------------------------
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(
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

        Account existingAccount = accountService.findAccountID(id);

        if (existingAccount == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        accountService.deleteAccount(id);

        return Response
            .ok("{\"message\":\"Account deleted successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}