/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.exception.mappers;

import com.bookstore.exception.OutOfStockException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.json.*;

@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {
    @Override
    public Response toResponse(OutOfStockException ex) {
        JsonObject error = Json.createObjectBuilder()
            .add("error", "Out of Stock")
            .add("message", ex.getMessage())
            .build();
        return Response.status(Response.Status.CONFLICT)
            .entity(error)
            .build();
    }
}
