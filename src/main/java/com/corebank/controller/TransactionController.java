package com.corebank.controller;

import java.util.List;

import com.corebank.model.Transaction;
import com.corebank.service.TransactionService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController() {
        transactionService = new TransactionService();
    }


    // ------------------------------ DEPOSIT ------------------------------

    @POST
    @Path("/deposit/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(
            @PathParam("accountId") int accountId,
            @QueryParam("amount") Double amount) {

        if (amount == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Amount is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (amount <= 0) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Amount must be greater than zero.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        boolean success =
            transactionService.deposit(accountId, amount);

        if (!success) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Deposit could not be completed.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok("{\"message\":\"Deposit completed successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    @POST
    @Path("/deposit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositWithoutAccountId() {

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity("{\"error\":\"Account ID is required.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }


    // ------------------------------ WITHDRAWAL ------------------------------

    @POST
    @Path("/withdrawal/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawal(
            @PathParam("accountId") int accountId,
            @QueryParam("amount") Double amount) {

        if (amount == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Amount is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (amount <= 0) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Amount must be greater than zero.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        boolean success =
            transactionService.withdrawal(accountId, amount);

        if (!success) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Withdrawal could not be completed.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok("{\"message\":\"Withdrawal completed successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    @POST
    @Path("/withdrawal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawalWithoutAccountId() {

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity("{\"error\":\"Account ID is required.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    // ------------------------------ LIST ------------------------------

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> listTransactions() {
        return transactionService.listTransactions();
    }


    // ------------------------------ FIND BY ID ------------------------------

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTransactionById(@PathParam("id") int id) {

        Transaction transaction =
            transactionService.findTransactionID(id);

        if (transaction == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Transaction not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response.ok(transaction).build();
    }


    // ------------------------------ LIST BY ACCOUNT ------------------------------

    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> listTransactionsByAccount(
            @PathParam("accountId") int accountId) {

        return transactionService
            .listTransactionsByAccount(accountId);
    }
}