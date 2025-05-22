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


@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    public static Map<Integer, Cart> carts = new HashMap<>();

    // Add item to cart
    @POST
    @Path("/items")
    public Response addItemToCart(
            @PathParam("customerId") int customerId,
            CartItemRequest itemRequest) {

        // Validate customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }

        // Validate book exists
        if (!BookResource.books.containsKey(itemRequest.getBookId())) {
            throw new BookNotFoundException("Book with ID " + itemRequest.getBookId() + " does not exist.");
        }
         
        // Validate quantity is not negative
        if (itemRequest.getQuantity() < 0) {
            throw new InvalidInputException("Quantity cannot be negative");
        }

        // Validate quantity does not exceed stock
        Book book = BookResource.books.get(itemRequest.getBookId());
        if (itemRequest.getQuantity() > book.getStock()) {
            throw new InvalidInputException("Quantity exceeds available stock");
        }

        Cart cart = carts.computeIfAbsent(customerId, k -> new Cart(customerId));
        
        // Find existing item
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getBookId() == itemRequest.getBookId())
                .findFirst();

        if (existingItem.isPresent()) {
            int newQuantity = existingItem.get().getQuantity() + itemRequest.getQuantity();
            if (newQuantity > book.getStock()) {
                throw new InvalidInputException("Quantity exceeds available stock");
            }
            existingItem.get().setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem(
                    cart.getItems().size() + 1,  // Simple ID generation
                    itemRequest.getBookId(),
                    itemRequest.getBookTitle(),
                    itemRequest.getQuantity()
            );
            cart.getItems().add(newItem);
        }

        return Response.ok(cart).build();
    }

    // Get cart
    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        // Validate customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }
        return Response.ok(cart).build();
    }

    // Update cart item
    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId,
            QuantityUpdateRequest request) {

        // Validate customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID " + customerId);
        }

        // Validate book exists
        Book book = BookResource.books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }

        // Validate quantity is not negative
        if (request.getQuantity() < 0) {
            throw new InvalidInputException("Quantity cannot be negative");
        }

        // Validate quantity does not exceed stock
        if (request.getQuantity() > book.getStock()) {
            throw new InvalidInputException("Quantity exceeds available stock");
        }

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getBookId() == bookId)
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Cart item for book ID " + bookId + " does not exist."));

        item.setQuantity(request.getQuantity());
        return Response.ok(cart).build();
    }

    // Remove cart item
    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId) {

        // Validate customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID " + customerId);
        }

        boolean removed = cart.getItems().removeIf(item -> item.getBookId() == bookId);
        if (!removed) {
            throw new CartItemNotFoundException("Cart item for book ID " + bookId + " does not exist.");
        }

        return Response.noContent().build();
    }

    // DTOs
    public static class CartItemRequest {
        private int bookId;
        private String bookTitle;
        private int quantity;

        // Getters/setters
        public int getBookId() { return bookId; }
        public void setBookId(int bookId) { this.bookId = bookId; }
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class QuantityUpdateRequest {
        private int quantity;

        // Getters/setters
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}

