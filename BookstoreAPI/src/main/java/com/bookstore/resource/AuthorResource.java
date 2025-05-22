/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.exception.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.stream.Collectors;


@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    public static Map<Integer, Author> authors = new HashMap<>();
    static int idCounter = 1;

    @POST
    public Response createAuthor(Author author, @Context UriInfo uriInfo) {
        // Validate name
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }

        author.setId(idCounter++);
        authors.put(author.getId(), author);
        
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(author.getId()));
        return Response.created(builder.build()).entity(author).build();
    }

    @GET
    public Response getAllAuthors() {
        return Response.ok(new ArrayList<>(authors.values())).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthor(@PathParam("id") int id) {
        Author author = authors.get(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        Author existing = authors.get(id);
        if (existing == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }

        // Validate name
        if (updatedAuthor.getName() == null || updatedAuthor.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }
        
        // Preserve existing biography if not provided in update
        if (updatedAuthor.getBiography() == null) {
            updatedAuthor.setBiography(existing.getBiography());
        }
        
        updatedAuthor.setId(id);
        authors.put(id, updatedAuthor);
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author removed = authors.remove(id);
        if (removed == null) {
            throw new AuthorNotFoundException("Cannot delete - Author with ID " + id + " not found");
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        
        List<Book> authorBooks = BookResource.books.values().stream()
                .filter(book -> book.getAuthorId() == id)
                .collect(Collectors.toList());
        
        return Response.ok(authorBooks).build();
    }
}
