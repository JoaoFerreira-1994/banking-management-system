package com.corebank.controller;

import com.corebank.model.Employee;
import com.corebank.model.User;
import com.corebank.service.EmployeeService;
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

@Path("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController() {
        employeeService = new EmployeeService();
    }


    // ------------------------------ LIST ------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listEmployees(
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
            .ok(employeeService.listEmployees())
            .build();
    }


    // ------------------------------ FIND BY ID ------------------------------
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEmployeeById(
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

        Employee employee = employeeService.findEmployeeID(id);

        if (employee == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Employee not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response.ok(employee).build();
    }


    // ------------------------------ CREATE ------------------------------
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEmployee(
            Employee employee,
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

        boolean created = employeeService.createEmployee(employee);

        if (!created) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Invalid employee data.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .status(Response.Status.CREATED)
            .entity(employee)
            .build();
    }


    // ------------------------------ UPDATE ------------------------------
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(
            @PathParam("id") int id,
            Employee employee,
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

        boolean updated =
            employeeService.updateEmployee(id, employee);

        if (!updated) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Employee could not be updated.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response.ok(employee).build();
    }


    // ------------------------------ DELETE ------------------------------
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(
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

        boolean deleted = employeeService.deleteEmployee(id);

        if (!deleted) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Employee not found.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

        return Response
            .ok("{\"message\":\"Employee deleted successfully.\"}")
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}