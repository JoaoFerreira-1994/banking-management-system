package com.corebank.controller;

import java.util.List;

import com.corebank.model.Account;
import com.corebank.model.Transfer;
import com.corebank.model.User;
import com.corebank.service.AccountService;
import com.corebank.service.TransferService;
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

@Path("/transfers")
public class TransferController {

    private TransferService transferService;
    private AccountService accountService;

    public TransferController() {
        transferService = new TransferService();
        accountService = new AccountService();
    }


    // ------------------------------ TRANSFER ------------------------------
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(
            @QueryParam("sourceIban") String sourceIban,
            @QueryParam("destinationIban") String destinationIban,
            @QueryParam("amount") Double amount,
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (!AuthUtil.isClient(user) && !AuthUtil.isEmployee(user)) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (sourceIban == null || sourceIban.isBlank() ||
            destinationIban == null || destinationIban.isBlank() ||
            amount == null || !Double.isFinite(amount) || amount <= 0) {

            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Invalid transfer details.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        sourceIban = sourceIban.replaceAll("\\s+", "").toUpperCase();
        destinationIban = destinationIban.replaceAll("\\s+", "").toUpperCase();

        Account sourceAccount =
            accountService.findAccountByIban(sourceIban);

        Account destinationAccount =
            accountService.findAccountByIban(destinationIban);

        if (sourceAccount == null || destinationAccount == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (sourceAccount.getId() == destinationAccount.getId()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Source and destination accounts must be different.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (AuthUtil.isClient(user)) {

            if (user.getClientId() == null ||
                !user.getClientId().equals(sourceAccount.getClientId())) {

                return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Access denied.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }
        }

        boolean success = transferService.transfer(
            sourceAccount.getId(),
            destinationAccount.getId(),
            amount
        );

        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Transfer could not be completed.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response.ok(
            "{\"message\":\"Transfer completed successfully.\"}"
        )
        .type(MediaType.APPLICATION_JSON)
        .build();
    }


    // ------------------------------ TRANSFER BY IBAN ------------------------------

    @POST
    @Path("/iban")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferByIban(
            @QueryParam("sourceAccountId") Integer sourceAccountId,
            @QueryParam("destinationIban") String destinationIban,
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

        if (!AuthUtil.isClient(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (sourceAccountId == null ||
            destinationIban == null ||
            destinationIban.isBlank() ||
            amount == null ||
            !Double.isFinite(amount) ||
            amount <= 0) {

            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Invalid transfer details.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        Account sourceAccount =
            accountService.findAccountID(sourceAccountId);

        if (sourceAccount == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Source account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (user.getClientId() == null ||
            !user.getClientId().equals(sourceAccount.getClientId())) {

            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        String normalizedIban =
            destinationIban.replaceAll("\\s+", "").toUpperCase();

        Account destinationAccount =
            accountService.findAccountByIban(normalizedIban);

        if (destinationAccount == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Destination account not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (sourceAccountId == destinationAccount.getId()) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Source and destination accounts must be different.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        boolean success = transferService.transfer(
            sourceAccountId,
            destinationAccount.getId(),
            amount
        );

        if (!success) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Transfer could not be completed.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok("{\"message\":\"Transfer completed successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }


    // ------------------------------ LIST ------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTransfers(
            @HeaderParam("Authorization") String authorization) {

        User user = AuthUtil.getAuthenticatedUser(authorization);

        if (user == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Unauthorized.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        // Full transfer list is only available to employees
        if (!AuthUtil.isEmployee(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity("{\"error\":\"Access denied.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        List<Transfer> transfers =
            transferService.listTransfers();

        return Response
            .ok(transfers)
            .build();
    }


    // ------------------------------ FIND BY ID ------------------------------
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTransferById(
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

        Transfer transfer =
            transferService.findTransferID(id);

        if (transfer == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Transfer not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (AuthUtil.isClient(user)) {

            Account sourceAccount =
                accountService.findAccountID(
                    transfer.getSourceAccountId()
                );

            Account destinationAccount =
                accountService.findAccountID(
                    transfer.getDestinationAccountId()
                );

            boolean sourceBelongsToClient =
                sourceAccount != null &&
                user.getClientId() != null &&
                user.getClientId() == sourceAccount.getClientId();

            boolean destinationBelongsToClient =
                destinationAccount != null &&
                user.getClientId() != null &&
                user.getClientId() == destinationAccount.getClientId();

            if (!sourceBelongsToClient &&
                !destinationBelongsToClient) {

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
            .ok(transfer)
            .build();
    }
}