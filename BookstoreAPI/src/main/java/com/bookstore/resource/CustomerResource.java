/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.model.Customer;
import com.bookstore.exception.CustomerNotFoundException;
import com.bookstore.exception.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    public static Map<Integer, Customer> customers = new HashMap<>();
    private static int idCounter = 1;

    @POST
    public Response createCustomer(Customer customer, @Context UriInfo uriInfo) {
        // Validate required fields
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required.");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email is required.");
        }

        customer.setId(idCounter++);
        customers.put(customer.getId(), customer);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(customer.getId()));
        return Response.created(builder.build()).entity(customer).build();
    }

    @GET
    public Response getAllCustomers() {
        return Response.ok(new ArrayList<>(customers.values())).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") int id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        Customer existing = customers.get(id);
        if (existing == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        // Validate required fields
        if (updatedCustomer.getName() == null || updatedCustomer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required.");
        }

        updatedCustomer.setId(id);
        customers.put(id, updatedCustomer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer removed = customers.remove(id);
        if (removed == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return Response.noContent().build();
    }
}

