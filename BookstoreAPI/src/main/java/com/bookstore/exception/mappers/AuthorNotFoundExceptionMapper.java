/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.exception.mappers;

import com.bookstore.exception.AuthorNotFoundException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.json.*;

@Provider
public class AuthorNotFoundExceptionMapper implements ExceptionMapper<AuthorNotFoundException> {
    @Override
    public Response toResponse(AuthorNotFoundException ex) {
        JsonObject error = Json.createObjectBuilder()
            .add("error", "Author Not Found")
            .add("message", ex.getMessage())
            .build();
        return Response.status(Response.Status.NOT_FOUND)
            .entity(error)
            .build();
    }
}
