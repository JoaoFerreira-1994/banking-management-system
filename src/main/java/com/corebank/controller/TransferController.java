package com.corebank.controller;

import com.corebank.service.TransferService;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transfers")
public class TransferController {

    private TransferService transferService;

    public TransferController() {
        transferService = new TransferService();
    }


    // ------------------------------ TRANSFER ------------------------------

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(
            @QueryParam("sourceAccountId") Integer sourceAccountId,
            @QueryParam("destinationAccountId") Integer destinationAccountId,
            @QueryParam("amount") Double amount) {

        if (sourceAccountId == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Source account ID is required.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        if (destinationAccountId == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Destination account ID is required.\"}")
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

        boolean success = transferService.transfer(
            sourceAccountId,
            destinationAccountId,
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
}