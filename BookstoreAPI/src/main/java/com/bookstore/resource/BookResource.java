/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.model.Book;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.exception.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    public static Map<Integer, Book> books = new HashMap<>();
    private static int idCounter = 1;

    // Adjust this to your current academic year if needed
    private static final int CURRENT_YEAR = 2025;

    @POST
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        // Validate title
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Title is required");
        }
        // Validate author exists
        if (!AuthorResource.authors.containsKey(book.getAuthorId())) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        // Validate publication year
        if (book.getPublicationYear() > CURRENT_YEAR) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        // Validate ISBN
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN is required");
        }
        if (!book.getIsbn().matches("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$|^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$")) {
            throw new InvalidInputException("Invalid ISBN format");
        }
        // Validate stock
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
        // Validate price
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than 0");
        }

        book.setId(idCounter++);
        books.put(book.getId(), book);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(book.getId()));
        return Response.created(builder.build()).entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(new ArrayList<>(books.values())).build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") int id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        Book existing = books.get(id);
        if (existing == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        // Validate author exists
        if (!AuthorResource.authors.containsKey(updatedBook.getAuthorId())) {
            throw new AuthorNotFoundException("Author with ID " + updatedBook.getAuthorId() + " does not exist.");
        }
        // Validate publication year
        if (updatedBook.getPublicationYear() > CURRENT_YEAR) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        // Validate title
        if (updatedBook.getTitle() == null || updatedBook.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Title is required");
        }
        // Validate ISBN
        if (updatedBook.getIsbn() == null || updatedBook.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN is required");
        }
        if (!updatedBook.getIsbn().matches("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$|^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$")) {
            throw new InvalidInputException("Invalid ISBN format");
        }
        // Validate stock
        if (updatedBook.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
        // Validate price
        if (updatedBook.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than 0");
        }

        updatedBook.setId(id);
        books.put(id, updatedBook);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book removed = books.remove(id);
        if (removed == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.noContent().build();
    }
}
