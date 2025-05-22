/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.exception.mappers;

import com.bookstore.exception.CartItemNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CartItemNotFoundExceptionMapper implements ExceptionMapper<CartItemNotFoundException> {

    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse() {} // Required for JAX-RS

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        // Getters and setters
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    @Override
    public Response toResponse(CartItemNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            "Cart Item Not Found",
            ex.getMessage()
        );
        
        return Response.status(Response.Status.NOT_FOUND)
            .entity(response)
            .build();
    }
}

