package com.corebank.controller;

import java.util.List;

import com.corebank.model.Account;
import com.corebank.model.Payment;
import com.corebank.model.User;
import com.corebank.service.AccountService;
import com.corebank.service.PaymentService;
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

@Path("/payments")
public class PaymentController {

    private PaymentService paymentService;
    private AccountService accountService;

    public PaymentController() {
        paymentService = new PaymentService();
        accountService = new AccountService();
    }


    // ------------------------------ LIST ------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPayments(
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

        List<Payment> payments =
            paymentService.listPayments();

        return Response
            .ok(payments)
            .build();
    }


    // ------------------------------ FIND BY ID ------------------------------
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPaymentById(
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

        Payment payment =
            paymentService.findPaymentID(id);

        if (payment == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Payment not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (AuthUtil.isClient(user)) {

            Account account =
                accountService.findAccountID(payment.getAccountId());

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
            .ok(payment)
            .build();
    }


    // ------------------------------ PAYMENT ------------------------------
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response makePayment(
            @QueryParam("accountId") Integer accountId,
            @QueryParam("entity") String entity,
            @QueryParam("reference") String reference,
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

        if (accountId == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Account ID is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (entity == null || entity.trim().isEmpty()) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Entity is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (reference == null || reference.trim().isEmpty()) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Reference is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (amount == null || !Double.isFinite(amount) || amount <= 0) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Amount must be greater than zero.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        // CLIENT can only make payments from their own account
        if (AuthUtil.isClient(user)) {

            Account account =
                accountService.findAccountID(accountId);

            if (account == null) {
                return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Account not found.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }

            if (user.getClientId() == null ||
                !user.getClientId().equals(account.getClientId())) {

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

        boolean success = paymentService.makePayment(
            accountId,
            entity,
            reference,
            amount
        );

        if (!success) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Payment could not be completed.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok("{\"message\":\"Payment completed successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}