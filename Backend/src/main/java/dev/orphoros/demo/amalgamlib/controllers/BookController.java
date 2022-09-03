package dev.orphoros.demo.amalgamlib.controllers;

import dev.orphoros.demo.amalgamlib.model.Book;
import dev.orphoros.demo.amalgamlib.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Book controller
 * <p>
 * This class is responsible for handling GET, PUT, POST and DELETE requests.
 * <p>
 * The book controller is being mapped under the <em>/books</em> path. The controller can only
 * work with Json objects
 *
 * @author Orphoros
 * @version 1.1
 * @see Book Book
 * @see BookService BookService
 * @see dev.orphoros.demo.amalgamlib.repository.BookRepository BookRepository
 */
@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookController {
    private final BookService bookService;

    /**
     * Main constructor of the Book Controller
     * @param bookService Service for processing requests and checking for exceptions
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * GET Mapping for Book using ISBN
     * <p>
     * Allows to retrieve a book by its ISBN from the central database.
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A book may be accessed by calling {@code /books/012345} where
     * the number in the path is the ISBN of the book to retrieve</dd>
     * </dl>
     * @param ISBN path variable ID of the book to retrieve
     * @return {@link Book} by its corresponding ISBN
     */
    @GetMapping("/{ISBN}")
    public Book getBookByISBN(@PathVariable Long ISBN){
        return this.bookService.getBookByISBN(ISBN);
    }

    /**
     * GET Mapping for Book
     * <p>
     * Allows to retrieve a list of books based on request parameters. All the parameters
     * are optional and one may select any number of them in any order in the request.
     * @param authID Optional request parameter to filter books with author ID
     * @param title Optional request parameter to filter by title containing a keyword
     * @param forKids Optional request parameter to filter books that are meant for kids or not
     * @param publishYear Optional request parameter to filter books by publish year
     * @return {@link List} of found books based on the request parameters
     */
    @GetMapping
    public List<Book> getAllBooks (@RequestParam(required = false) Long authID, @RequestParam(required = false) String title, @RequestParam(required = false) Boolean forKids, @RequestParam(required = false) Integer publishYear){
        return this.bookService.getFilteredBooks(authID, title, forKids, publishYear);
    }

    /**
     * POST Mapping for Books
     * <p>
     * Allows to add a new Book to the database using Json.
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A new book may be provided in the request body of the POST request in
     * Json format under the path {@code /books}.
     * </dd>
     * </dl>
     *
     * @param b Book to add to the database in Json format from the request body
     * @return {@link Book} copy of the newly saved book if succeeded
     */
    @PostMapping(consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Book addNewBook(@RequestBody Book b) {
        return this.bookService.addNewBook(b);
    }

    /**
     * DELETE Mapping for Book
     * <p>
     * Allows to delete an existing book using its ISBN
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A book may be deleter under {@code /books/012345} where the number in the path
     * is the ISBN of the book to delete
     * </dd>
     * </dl>
     *
     * @param ISBN path variable ID of the book to delete
     */
    @DeleteMapping("/{ISBN}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long ISBN){
        this.bookService.deleteBook(ISBN);
    }

    /**
     * PUT Mapping for Book
     * <p>
     * Allows to update an existing book's details using its corresponding ISBN
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A book may be updated under the path {@code /books/012345} where the number in the path
     * is the ID of the book to update. A request body containing the same ISBN must be present as well!
     * </dd>
     * </dl>
     *
     * @param ISBN path variable ID of the existing book to update
     * @param b Json formatted book in the request body with the updated details containing the ISBN
     * @return {@link Book} copy of the saved and updated book if succeeded
     */
    @PutMapping("/{ISBN}")
    public Book updateBook(@PathVariable Long ISBN, @RequestBody Book b){
        return this.bookService.updateBook(ISBN, b);
    }
}
