package com.webFlux.cosmos.SpringRepo.resilience.model;

import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveCosmosRepository<Book,String> {

    @Override
    Flux<Book> findAll();

    @Query(value = "select * from c where c.category=@category")
    Flux<Book> findByCategoryQuery(String category);

    Flux<Book> findByCategory(String category);

    Flux<Book> findByIdAndAuthor(String id, String author);

    Flux<Book> findByAuthor(String author);

    Flux<Book> findByIsbn(String isbn);

    @Query("select * from c where c.category = @category")
    Slice<Book> findByCategoryPage(String category, Pageable pageable);
}
