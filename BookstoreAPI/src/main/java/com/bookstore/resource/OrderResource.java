/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.model.*;
import com.bookstore.exception.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static Map<Integer, List<Order>> ordersByCustomer = new HashMap<>();
    private static int orderIdCounter = 1;

    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        // Validate customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }

        // Get cart from CartResource
        Cart cart = CartResource.carts.get(customerId);
        
        // Validate cart exists and has items
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new InvalidInputException("Cart is empty");
        }

        // Calculate total and validate books
        double total = 0.0;
        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Book book = BookResource.books.get(item.getBookId());
            if (book == null) {
                throw new BookNotFoundException("Book with ID " + item.getBookId() + " not found");
            }
            
            // Validate stock
            if (item.getQuantity() > book.getStock()) {
                throw new InvalidInputException("Insufficient stock for book ID " + item.getBookId());
            }
            
            total += item.getQuantity() * book.getPrice();
            orderItems.add(new CartItem(item.getId(), item.getBookId(), item.getBookTitle(), item.getQuantity()));
            
            // Update book stock
            book.setStock(book.getStock() - item.getQuantity());
        }

        // Create and store order
        Order order = new Order(orderIdCounter++, customerId, orderItems, total);
        ordersByCustomer.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
        
        // Clear the cart
        cart.getItems().clear();
        
        return Response.ok(order).build();
    }

    @GET
    public Response getOrders(@PathParam("customerId") int customerId) {
        // Check if customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        List<Order> orders = ordersByCustomer.getOrDefault(customerId, Collections.emptyList());
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(
            @PathParam("customerId") int customerId,
            @PathParam("orderId") int orderId) {
        
        List<Order> orders = ordersByCustomer.get(customerId);
        if (orders == null) {
            throw new OrderNotFoundException("No orders found for customer ID " + customerId);
        }

        return orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst()
                .map(Response::ok)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " does not exist"))
                .build();
    }
}
