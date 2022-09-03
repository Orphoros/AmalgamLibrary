package dev.orphoros.demo.amalgamlib.repository;

import dev.orphoros.demo.amalgamlib.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Book Repository
 * <p>
 * This class is responsible for communicating with the external database with book data
 * <p>
 * Each function in this class is a custom defined command for the
 * {@link dev.orphoros.demo.amalgamlib.services.BookService BookService} class to issue
 * SQL commands to the database. Updates and changes in this class must/should be represented in the {@code BookService}
 * class respectively.
 *
 * @author Orphoros
 * @version 1.1
 * @see Book Book
 * @see dev.orphoros.demo.amalgamlib.services.BookService BookService
 * @see dev.orphoros.demo.amalgamlib.controllers.BookController BookController
 */
@Repository
public interface BookRepository  extends CrudRepository<Book, Long> {
    /**
     * Check if a book exists in the database or not
     * @param ISBN ID of the book to find. Should not be null!
     * @return True if book exists
     */
    boolean existsByISBN(Long ISBN);

    //==============================[SECTION FOR FILTERING WITHOUT AUTHOR ID]==============================

    /**
     * Find every book in the database
     * @return {@link List} of all found books
     */
    @Override
    List<Book> findAll();

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingIgnoreCase(String titlePart);

    /**
     * Find books by filter parameters
     * @param forKids True if book is readable by kids.
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByForKids(Boolean forKids);

    /**
     * Find books by filter parameters
     * @param year Publish year of the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByPublishYear(int year);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param forKinds True if book is readable by kids.
     * @param year Publish year of the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingIgnoreCaseAndForKidsAndPublishYear(String titlePart, Boolean forKinds, Integer year);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param forKids True if book is readable by kids.
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingIgnoreCaseAndForKids(String titlePart, Boolean forKids);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param year Publish year of the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingAndPublishYear(String titlePart, Integer year);

    /**
     * Find books by filter parameters
     * @param year Publish year of the book
     * @param forKids True if book is readable by kids.
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByPublishYearAndForKids(Integer year, Boolean forKids);

    //==============================[SECTION FOR FILTERING WITH AUTHOR ID]==============================

    /**
     * Find books by filter parameters
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByAuthor_Id(Long author_id);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingIgnoreCaseAndAuthor_Id(String titlePart, Long author_id);

    /**
     * Find books by filter parameters
     * @param forKids True if book is readable by kids.
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByForKidsAndAuthor_Id(Boolean forKids, Long author_id);

    /**
     * Find books by filter parameters
     * @param year Publish year of the book
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByPublishYearAndAuthor_Id(int year, Long author_id);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param forKinds True if book is readable by kids.
     * @param year Publish year of the book
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingIgnoreCaseAndForKidsAndPublishYearAndAuthor_Id(String titlePart, Boolean forKinds, Integer year, Long author_id);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param forKids True if book is readable by kids.
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingIgnoreCaseAndForKidsAndAuthor_Id(String titlePart, Boolean forKids, Long author_id);

    /**
     * Find books by filter parameters
     * @param titlePart Keyword to be present in the title (non-case sensitive)
     * @param year Publish year of the book
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByTitleContainingAndPublishYearAndAuthor_Id(String titlePart, Integer year, Long author_id);

    /**
     * Find books by filter parameters
     * @param year Publish year of the book
     * @param forKids True if book is readable by kids.
     * @param author_id ID of the author who wrote the book
     * @return {@link List} of found books by filter
     */
    List<Book> findAllByPublishYearAndForKidsAndAuthor_Id(Integer year, Boolean forKids, Long author_id);
}
