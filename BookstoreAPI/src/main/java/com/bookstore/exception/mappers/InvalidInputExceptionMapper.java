/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.exception.mappers;

import com.bookstore.exception.InvalidInputException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.json.*;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException ex) {
        JsonObject error = Json.createObjectBuilder()
            .add("error", "Invalid Input")
            .add("message", ex.getMessage())
            .build();
        return Response.status(Response.Status.BAD_REQUEST) // 400
            .entity(error)
            .build();
    }
}
