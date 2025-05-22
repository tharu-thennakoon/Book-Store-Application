package com.bookstore.util;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.resource.AuthorResource;
import com.bookstore.resource.BookResource;
import com.bookstore.resource.CustomerResource;

public class DefaultDataInitializer {
    public static void initializeDefaultData() {
        // Initialize default authors
        Author author1 = new Author();
        author1.setId(1);
        author1.setName("J.K. Rowling");
        author1.setBiography("British author best known for the Harry Potter series");
        AuthorResource.authors.put(author1.getId(), author1);

        Author author2 = new Author();
        author2.setId(2);
        author2.setName("George R.R. Martin");
        author2.setBiography("American novelist and short story writer, best known for A Song of Ice and Fire series");
        AuthorResource.authors.put(author2.getId(), author2);

        Author author3 = new Author();
        author3.setId(3);
        author3.setName("Stephen King");
        author3.setBiography("American author of horror, supernatural fiction, suspense, and fantasy novels");
        AuthorResource.authors.put(author3.getId(), author3);

        // Initialize default books
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setAuthorId(1);
        book1.setIsbn("978-0747532743");
        book1.setPublicationYear(1997);
        book1.setPrice(19.99);
        book1.setStock(50);
        BookResource.books.put(book1.getId(), book1);

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("A Game of Thrones");
        book2.setAuthorId(2);
        book2.setIsbn("978-0553103540");
        book2.setPublicationYear(1996);
        book2.setPrice(24.99);
        book2.setStock(30);
        BookResource.books.put(book2.getId(), book2);

        Book book3 = new Book();
        book3.setId(3);
        book3.setTitle("The Shining");
        book3.setAuthorId(3);
        book3.setIsbn("978-0385121675");
        book3.setPublicationYear(1977);
        book3.setPrice(15.99);
        book3.setStock(40);
        BookResource.books.put(book3.getId(), book3);

        // Initialize default customers
        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");
        CustomerResource.customers.put(customer1.getId(), customer1);

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Jane Smith");
        customer2.setEmail("jane.smith@example.com");
        CustomerResource.customers.put(customer2.getId(), customer2);

        Customer customer3 = new Customer();
        customer3.setId(3);
        customer3.setName("Bob Johnson");
        customer3.setEmail("bob.johnson@example.com");
        CustomerResource.customers.put(customer3.getId(), customer3);
    }
} 