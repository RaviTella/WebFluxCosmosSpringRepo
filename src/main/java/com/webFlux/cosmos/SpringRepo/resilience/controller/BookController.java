package com.webFlux.cosmos.SpringRepo.resilience.controller;

import com.azure.cosmos.models.PartitionKey;

import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import com.webFlux.cosmos.SpringRepo.resilience.model.Book;
import com.webFlux.cosmos.SpringRepo.resilience.model.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    @RequestMapping(value = "/books/{id}/category/{category}", method = RequestMethod.GET)
    Mono<Book> getBookByIDAndCategory(@PathVariable String category, @PathVariable String id) {
        return bookRepository
                .findById(id, new PartitionKey(category))
                .map(book -> {
                    logger.info("Retrieved the book {}", book);
                    return book;
                });
    }

    @RequestMapping(value = "books/category/{category}", method = RequestMethod.GET)
    Flux<Book> getBookByCategory(@PathVariable String category) {
        final CosmosPageRequest pageRequest = new CosmosPageRequest(0, 1, null);
        Flux<Book> books = bookRepository
                .findByCategory(category);
        return books;
    }


    @RequestMapping(value = "books/category/{category}/pageIndex/{pageIndex}/pageSize/{pageSize}", method = RequestMethod.GET)
    Slice<Book> getBookByCategoryByPage(@PathVariable String category, @PathVariable int pageIndex, @PathVariable int pageSize) {
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageIndex, pageSize, null);
        Slice<Book> books = bookRepository
                .findByCategoryPage(category,pageRequest);
        return books;
    }


    @RequestMapping(value = "books/author/{author}", method = RequestMethod.GET)
    Flux<Book> getBookByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    @RequestMapping(value = "books/isbn/{isbn}", method = RequestMethod.GET)
    Flux<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

}

