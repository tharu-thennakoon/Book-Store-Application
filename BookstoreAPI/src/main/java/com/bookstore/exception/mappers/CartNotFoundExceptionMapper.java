/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.exception.mappers;

import com.bookstore.exception.CartNotFoundException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.json.*;


@Provider
public class CartNotFoundExceptionMapper implements ExceptionMapper<CartNotFoundException> {
    @Override
    public Response toResponse(CartNotFoundException ex) {
        JsonObject error = Json.createObjectBuilder()
            .add("error", "Cart Not Found")
            .add("message", ex.getMessage())
            .build();
        return Response.status(Response.Status.NOT_FOUND)
            .entity(error)
            .build();
    }
}
