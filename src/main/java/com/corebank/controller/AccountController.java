package com.corebank.controller;

import java.util.List;

import com.corebank.model.Account;
import com.corebank.service.AccountService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
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
    public List<Account> listAccounts() {
        return accountService.listAccounts();
    }


    // ------------------------------ FIND BY ID ------------------------------

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAccountById(@PathParam("id") int id) {

        Account account = accountService.findAccountID(id);

        if (account == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response.ok(account).build();
    }

    // ------------------------------ CREATE ------------------------------

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {

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
            Account account) {

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

        return Response.ok(updatedAccount).build();
    }

    // ------------------------------ DELETE ------------------------------

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("id") int id) {

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