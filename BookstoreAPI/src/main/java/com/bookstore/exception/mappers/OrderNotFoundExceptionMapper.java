/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.exception.mappers;

import com.bookstore.exception.OrderNotFoundException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

@Provider
public class OrderNotFoundExceptionMapper implements ExceptionMapper<OrderNotFoundException> {
    
    public static class ErrorResponse {
        private String error;
        private String message;

        // Required no-arg constructor
        public ErrorResponse() {}

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
    public Response toResponse(OrderNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            "Order Not Found", 
            ex.getMessage()
        );
        
        return Response.status(Response.Status.NOT_FOUND)
            .entity(response)
            .build();
    }
}
