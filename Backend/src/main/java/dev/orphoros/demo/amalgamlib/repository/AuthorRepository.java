package dev.orphoros.demo.amalgamlib.repository;

import dev.orphoros.demo.amalgamlib.controllers.AuthorController;
import dev.orphoros.demo.amalgamlib.model.Author;
import dev.orphoros.demo.amalgamlib.services.AuthorService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author Repository
 * <p>
 * This class is responsible for communicating with the external database with author data
 * <p>
 * Each function in this class is a custom defined command for the {@link AuthorService} class to issue
 * SQL commands to the database. Updates and changes in this class must/should be represented in the {@code AuthorService}
 * class respectively.
 *
 * @author Orphoros
 * @version 1.0
 * @see Author Author
 * @see AuthorService AuthorService
 * @see AuthorController AuthorController
 */
@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    /**
     * Find every single author in the database and return them in a list
     * @return {@link List} of found authors
     */
    @Override
    List<Author> findAll();

    /**
     * Find every author in the database by their name containing a keyword
     * @param name Keyword to be present in the name (case sensitive)
     * @return {@link List} of found authors
     */
    List<Author> findAuthorsByNameContaining(String name);

    /**
     * Find every author in the database by their gender (male or female)
     * @param isMale True or false boolean value to indicate gender
     * @return {@link List} of found authors
     */
    List<Author> findAuthorsByMale(Boolean isMale);

    /**
     * Find every author in the database by gender and by their name containing a keyword at the same time
     * @param isMale True or false boolean value to indicate gender
     * @param name Keyword to be present in the name (case sensitive)
     * @return {@link List} of found authors
     */
    List<Author> findAuthorsByMaleAndNameContaining(Boolean isMale, String name);

    /**
     * Check if an author exists in the database or not
     * @param id ID of the author to find. Should not be null!
     * @return True if author exists
     */
    boolean existsAuthorById(Long id);
}
