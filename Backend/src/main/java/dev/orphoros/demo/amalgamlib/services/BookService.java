package dev.orphoros.demo.amalgamlib.services;

import dev.orphoros.demo.amalgamlib.model.Book;
import dev.orphoros.demo.amalgamlib.repository.AuthorRepository;
import dev.orphoros.demo.amalgamlib.repository.BookRepository;
import dev.orphoros.demo.amalgamlib.exceptions.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Book Service
 * <p>
 * This class is responsible validating parameters, checking for exceptions and connecting the
 * {@code BookController} with the {@code BookRepository}.
 * <p>
 * This class may throw HTTP status codes in the 400 range if an exception happened.
 * <p>
 * HTTP status codes in the 500 range may be thrown as well if an uncaught exception happens.
 *
 * @author Orphoros
 * @version 1.2
 * @see dev.orphoros.demo.amalgamlib.controllers.BookController BookController
 * @see dev.orphoros.demo.amalgamlib.services.BookService BookService
 * @see dev.orphoros.demo.amalgamlib.repository.BookRepository BookRepository
 */
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * Main constructor of the {@code BookService} class.
     * @param bookRepository Repository that belongs to Book
     * @param authorRepository Repository that belongs to the Author of a book
     */
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Gives a list of filtered books based on filter parameters
     * <p>
     * All parameters are optional for filtering
     * @param authID The ID of the author of the book
     * @param title Keyword to be present in the title (non-case sensitive)
     * @param forKids Filter on books that are readable by kids as well or not
     * @param publishYear Publish year of the book
     * @return {@link List} of found reviews
     * @throws BadRequestException if parameter validation fails
     */
    public List<Book> getFilteredBooks(Long authID, String title, Boolean forKids, Integer publishYear){
        //No param is given
        if (title == null && forKids == null && publishYear == null) return getAllBooks(authID);
        //All params are given
        if (title != null && forKids != null && publishYear != null) return getBooksByAllParam(authID, title, forKids, publishYear);
        //Only title and kinds params are given
        if (title != null && forKids != null) return getBooksByTitleAndMeantForKids(authID, title, forKids);
        //Only title and publish year params are given
        if (title != null && publishYear != null) return getBooksByTitleAndYear(authID, title, publishYear);
        //Only kids and title params are given
        if (title == null && forKids != null && publishYear != null) return getBooksByYearAndKids(authID, publishYear, forKids);
        //Only title param is given
        if (title != null) return getBooksByTitleContainingWord(authID, title);
        //Only forKids param is given
        if (forKids != null) return getBooksMeantForKids(authID, forKids);
        //Only year param is given
        return getBooksByPublishYear(authID, publishYear);
    }

    /**
     * Finds a Book by its corresponding ID
     * @param ISBN ID of the book to find
     * @return {@link Book} that belongs to the ISBN
     * @throws NotFoundException if the ISBN does not exist in the database
     * @throws BadRequestException if the ISBN is empty or incorrect
     */
    public Book getBookByISBN(Long ISBN){
        validateISBN(ISBN);
        Optional<Book> b = bookRepository.findById(ISBN);
        if(b.isPresent()) return b.get();
        else throw new NotFoundException("Could not find any book with " + ISBN + " ISBN");
    }

    /**
     * Save a new Book to the database
     * @param b Book object to save
     * @return {@link Book} object that has been successfully saved
     * @throws BadRequestException if the book object is not properly formatted
     * @throws NotFoundException if the book's parent author could not be found
     */
    public Book addNewBook (Book b) {
        validateISBN(b.getISBN());
        if(bookRepository.existsByISBN(b.getISBN())) throw new BadRequestException("Book is already in the database!");
        if(b.getAuthor() == null || b.getAuthor().getId() == null) throw new BadRequestException("Author ID must be provided in the request body to connect the book to an author!");
        if(b.getAuthor().getMale() != null || b.getAuthor().getName() != null || b.getAuthor().getBooks() != null) throw new BadRequestException("Only Author ID can be given!");
        validateBook(b);
        if(!authorRepository.existsAuthorById(b.getAuthor().getId()))  throw new NotFoundException("Author does not exist in the database!");
        return bookRepository.save(b);
    }

    /**
     * Delete an existing book from the database
     * @param ISBN ID of the existing book to be deleted
     * @throws BadRequestException if the ISBN is not properly formatted
     * @throws NotFoundException if the ISBN does not exist in the database
     */
    public void deleteBook(Long ISBN){
        validateISBN(ISBN);
        if(!bookRepository.existsByISBN(ISBN)) throw new NotFoundException("Book does not exist in the database!");
        bookRepository.deleteById(ISBN);
    }

    /**
     * Update the values of an existing review in the database
     * @param ISBN ID of the existing book object
     * @param b Book object with the updated values (ISBN must be present as well)
     * @return {@link Book} object with the new values that has been successfully updated
     * @throws BadRequestException if the book object is not properly formatted or the ISBNs are incorrect
     * @throws NotFoundException if the ISBNs or the book's author ID do not exist in the database
     */
    public Book updateBook(Long ISBN, Book b){
        validateISBN(ISBN);
        validateBook(b);
        if(!ISBN.equals(b.getISBN())) throw new BadRequestException("Provided ISBNs do not match!");
        if(b.getAuthor() != null) throw new BadRequestException("Book's author ID may not be changed once assigned!");

        //Update data
        return bookRepository.findById(ISBN)
                .map(book -> {
                    book.setPublishYear(b.getPublishYear());
                    book.setTitle(b.getTitle());
                    book.setForKids(b.getForKids());
                    book.setPublishYear(b.getPublishYear());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new NotFoundException("Book ISBN not found!"));
    }

    //=========================[Helper methods]=========================

    /**
     * Validates a book object's properties
     * @param b Book object to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateBook(Book b){
        if(b.getTitle() == null || b.getForKids() == null || b.getPublishYear() == null) throw new BadRequestException("Some required fields were not provided");
        if(b.getTitle().isEmpty()) throw new BadRequestException("Book title must be provided!");
        if(b.getReviews() != null) throw new BadRequestException("Book reviews cannot be given!");
        if((b.getPublishYear() > LocalDate.now().getYear()) || b.getPublishYear() < 0) throw new BadRequestException("Year must be between 0 and " + LocalDate.now().getYear());
    }

    /**
     * Validates the format of an ISBN
     * @param ISBN ID to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateISBN(Long ISBN){
        if(ISBN == null) throw new BadRequestException("Book ISBN is not defined!");
        if(ISBN < 0) throw new BadRequestException("Book ISBN must be a positive number");
    }

    // =========================[FILTERING]============================

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param titlePart Keyword to be present in the title
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksByTitleContainingWord(Long authID, String titlePart){
        if(authID == null) return bookRepository.findAllByTitleContainingIgnoreCase(titlePart);
        return bookRepository.findAllByTitleContainingIgnoreCaseAndAuthor_Id(titlePart, authID);
    }

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param forKids Books that are readable by kids as well or not
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksMeantForKids(Long authID, Boolean forKids){
        if(authID == null) return bookRepository.findAllByForKids(forKids);
        return bookRepository.findAllByForKidsAndAuthor_Id(forKids, authID);
    }

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param titlePart Keyword to be present in the title
     * @param forKids Books that are readable by kids as well or not
     * @param year Publish year of the book
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksByAllParam(Long authID, String titlePart, Boolean forKids, Integer year){
        if(authID == null) return bookRepository.findAllByTitleContainingIgnoreCaseAndForKidsAndPublishYear(titlePart,forKids,year);
        return bookRepository.findAllByTitleContainingIgnoreCaseAndForKidsAndPublishYearAndAuthor_Id(titlePart,forKids,year,authID);
    }

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param titlePart Keyword to be present in the title
     * @param forKids Books that are readable by kids as well or not
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksByTitleAndMeantForKids(Long authID, String titlePart, Boolean forKids){
        if(authID == null) return bookRepository.findAllByTitleContainingIgnoreCaseAndForKids(titlePart, forKids);
        return bookRepository.findAllByTitleContainingIgnoreCaseAndForKidsAndAuthor_Id(titlePart, forKids, authID);
    }

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param titlePart Keyword to be present in the title
     * @param year Publish year of the book
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksByTitleAndYear(Long authID, String titlePart, Integer year){
        if(authID == null) return bookRepository.findAllByTitleContainingAndPublishYear(titlePart, year);
        return bookRepository.findAllByTitleContainingAndPublishYearAndAuthor_Id(titlePart, year, authID);
    }

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param year Publish year of the book
     * @param forKids Books that are readable by kids as well or not
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksByYearAndKids(Long authID, Integer year, Boolean forKids){
        if(authID == null) return bookRepository.findAllByPublishYearAndForKids(year,forKids);
        return bookRepository.findAllByPublishYearAndForKidsAndAuthor_Id(year,forKids,authID);
    }

    /**
     * Gives results based on filter parameters.
     * @param authID ID of the author (optional, can be null)
     * @param year Publish year of the book
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getBooksByPublishYear(Long authID, Integer year){
        if(authID == null) return bookRepository.findAllByPublishYear(year);
        return bookRepository.findAllByPublishYearAndAuthor_Id(year,authID);
    }

    /**
     * Gives results based on author ID.
     * @param authID ID of the author (optional, can be null)
     * @return {@link List} of found Books. An empty list is returned if nothing is found.
     */
    private List<Book> getAllBooks(Long authID){
        if(authID == null) return bookRepository.findAll();
        return bookRepository.findAllByAuthor_Id(authID);
    }
}