package com.corebank.controller;

import java.util.List;

import com.corebank.model.Account;
import com.corebank.model.Transaction;
import com.corebank.model.User;
import com.corebank.service.AccountService;
import com.corebank.service.TransactionService;
import com.corebank.util.AuthUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
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
    private AccountService accountService;

    public TransactionController() {
        transactionService = new TransactionService();
        accountService = new AccountService();
    }


    // ------------------------------ DEPOSIT ------------------------------
    @POST
    @Path("/deposit/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(
            @PathParam("accountId") int accountId,
            @QueryParam("amount") Double amount,
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
    public Response depositWithoutAccountId(
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
            @QueryParam("amount") Double amount,
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
    public Response withdrawalWithoutAccountId(
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

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity("{\"error\":\"Account ID is required.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }


    // ------------------------------ LIST ------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTransactions(
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

        List<Transaction> transactions =
            transactionService.listTransactions();

        return Response
            .ok(transactions)
            .build();
    }


    // ------------------------------ FIND BY ID ------------------------------
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTransactionById(
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

        Transaction transaction =
            transactionService.findTransactionID(id);

        if (transaction == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Transaction not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (AuthUtil.isClient(user)) {

            Account account =
                accountService.findAccountID(transaction.getAccountId());

            if (account == null ||
                user.getClientId() == null ||
                user.getClientId() != account.getClientId()) {

                return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Access denied.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }
        }

        else if (!AuthUtil.isEmployee(user)) {

            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok(transaction)
            .build();
    }


    // ------------------------------ LIST BY ACCOUNT ------------------------------
    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTransactionsByAccount(
            @PathParam("accountId") int accountId,
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        Account account =
            accountService.findAccountID(accountId);

        if (account == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

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

        else if (!AuthUtil.isEmployee(user)) {

            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        List<Transaction> transactions =
            transactionService.listTransactionsByAccount(accountId);

        return Response
            .ok(transactions)
            .build();
    }
}