package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.exception.mappers.InvalidInputExceptionMapper;
import com.bookstore.exception.mappers.AuthorNotFoundExceptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import javax.json.JsonObject;
import static org.mockito.Mockito.*;
import java.util.List;

public class AuthorResourceTest {
    private AuthorResource authorResource;
    private UriInfo uriInfo;
    private UriBuilder uriBuilder;
    private InvalidInputExceptionMapper invalidInputMapper;
    private AuthorNotFoundExceptionMapper authorNotFoundMapper;

    @BeforeEach
    public void setUp() {
        authorResource = new AuthorResource();
        AuthorResource.authors.clear();
        AuthorResource.idCounter = 1;
        
        // Mock UriInfo and UriBuilder
        uriInfo = mock(UriInfo.class);
        uriBuilder = mock(UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        
        invalidInputMapper = new InvalidInputExceptionMapper();
        authorNotFoundMapper = new AuthorNotFoundExceptionMapper();
    }

    @Test
    public void testCreateAuthorWithMissingName() {
        Author author = new Author();
        author.setBiography("No name");

        try {
            authorResource.createAuthor(author, uriInfo);
            fail("Expected InvalidInputException to be thrown");
        } catch (InvalidInputException e) {
            Response response = invalidInputMapper.toResponse(e);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            JsonObject errorResponse = (JsonObject) response.getEntity();
            assertEquals("Invalid Input", errorResponse.getString("error"));
            assertEquals("Author name is required", errorResponse.getString("message"));
        }
    }

    @Test
    public void testCreateAuthorWithValidData() {
        Author author = new Author();
        author.setName("Test Author");
        author.setBiography("Test Biography");

        Response response = authorResource.createAuthor(author, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Author createdAuthor = (Author) response.getEntity();
        assertEquals(1, createdAuthor.getId());
        assertEquals("Test Author", createdAuthor.getName());
        assertEquals("Test Biography", createdAuthor.getBiography());
    }

    @Test
    public void testGetAuthorByValidId() {
        // First create an author
        Author author = new Author();
        author.setName("Test Author");
        author.setBiography("Test Biography");
        authorResource.createAuthor(author, uriInfo);

        Response response = authorResource.getAuthor(1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Author retrievedAuthor = (Author) response.getEntity();
        assertEquals(1, retrievedAuthor.getId());
        assertEquals("Test Author", retrievedAuthor.getName());
    }

    @Test
    public void testGetAuthorByInvalidId() {
        try {
            authorResource.getAuthor(999);
            fail("Expected AuthorNotFoundException to be thrown");
        } catch (AuthorNotFoundException e) {
            Response response = authorNotFoundMapper.toResponse(e);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            JsonObject errorResponse = (JsonObject) response.getEntity();
            assertEquals("Author Not Found", errorResponse.getString("error"));
            assertEquals("Author with ID 999 not found", errorResponse.getString("message"));
        }
    }

    @Test
    public void testUpdateAuthorWithValidData() {
        // First create an author
        Author author = new Author();
        author.setName("Original Name");
        author.setBiography("Original Bio");
        authorResource.createAuthor(author, uriInfo);

        // Update the author
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");
        updatedAuthor.setBiography("Updated Bio");

        Response response = authorResource.updateAuthor(1, updatedAuthor);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Author result = (Author) response.getEntity();
        assertEquals(1, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Bio", result.getBiography());
    }

    @Test
    public void testUpdateAuthorWithInvalidId() {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Test Name");
        updatedAuthor.setBiography("Test Bio");

        try {
            authorResource.updateAuthor(999, updatedAuthor);
            fail("Expected AuthorNotFoundException to be thrown");
        } catch (AuthorNotFoundException e) {
            Response response = authorNotFoundMapper.toResponse(e);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            JsonObject errorResponse = (JsonObject) response.getEntity();
            assertEquals("Author Not Found", errorResponse.getString("error"));
            assertEquals("Author with ID 999 does not exist", errorResponse.getString("message"));
        }
    }

    @Test
    public void testDeleteAuthorByValidId() {
        // First create an author
        Author author = new Author();
        author.setName("Test Author");
        author.setBiography("Test Bio");
        authorResource.createAuthor(author, uriInfo);

        Response response = authorResource.deleteAuthor(1);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteAuthorByInvalidId() {
        try {
            authorResource.deleteAuthor(999);
            fail("Expected AuthorNotFoundException to be thrown");
        } catch (AuthorNotFoundException e) {
            Response response = authorNotFoundMapper.toResponse(e);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            JsonObject errorResponse = (JsonObject) response.getEntity();
            assertEquals("Author Not Found", errorResponse.getString("error"));
            assertEquals("Cannot delete - Author with ID 999 not found", errorResponse.getString("message"));
        }
    }

    @Test
    public void testGetAllAuthors() {
        // Create multiple authors
        Author author1 = new Author();
        author1.setName("Author 1");
        author1.setBiography("Bio 1");
        authorResource.createAuthor(author1, uriInfo);

        Author author2 = new Author();
        author2.setName("Author 2");
        author2.setBiography("Bio 2");
        authorResource.createAuthor(author2, uriInfo);

        Response response = authorResource.getAllAuthors();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Author> authors = (List<Author>) response.getEntity();
        assertEquals(2, authors.size());
    }
} 