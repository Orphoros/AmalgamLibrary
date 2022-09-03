package dev.orphoros.demo.amalgamlib.controllers;

import dev.orphoros.demo.amalgamlib.model.Author;
import dev.orphoros.demo.amalgamlib.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author controller
 * <p>
 * This class is responsible for handling GET, PUT, POST and DELETE requests.
 * <p>
 * The author controller is being mapped under the <em>/authors</em> path. The controller can only
 * work with Json objects
 * 
 * @author Orphoros
 * @version 1.1
 * @see Author Author
 * @see AuthorService AuthorService
 * @see dev.orphoros.demo.amalgamlib.repository.AuthorRepository AuthorRepository
 */
@RestController
@RequestMapping("/authors")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthorController {
    private final AuthorService authorService;

    /**
     * Main constructor of the Author Controller
     *
     * @param authorService Service for processing requests and checking for exceptions
     */
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * GET Mapping for Author using author ID
     * <p>
     * Allows to retrieve an author from the central database.
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * An author may be accessed by calling {@code /authors/012345} where
     * the number in the path is the author ID.
     * </dd>
     * </dl>
     *
     * @param authID path variable ID of the author to retrieve
     * @return {@link Author} by its corresponding author ID
     */
    @GetMapping("/{authID}")
    public Author getAuthorByID(@PathVariable Long authID){
        return authorService.getAuthorByID(authID);
    }

    /**
     * GET Mapping for Author
     * <p>
     * Allows to retrieve a list of authors based on request parameters. All the parameters
     * are optional and one may select any number of them in any order in the request.
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A list of authors may be accessed by calling {@code /authors}.
     * </dd>
     * </dl>
     *
     * @param male Optional request parameter to filter by gender
     * @param name Optional request parameter to filter by name containing a keyword
     * @return {@link List} of found authors based on the request parameters
     */
    @GetMapping
    public List<Author> getAllAuthors(@RequestParam(required = false) Boolean male, @RequestParam(required = false) String name){
        return authorService.getFilteredAuthors(male,name);
    }

    /**
     * POST Mapping for Author
     * <p>
     * Allows to add a new Author to the database using Json.
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A new author may ge provided in the request body of the POST request in
     * Json format under the path {@code /authors}.
     * </dd>
     * </dl>
     *
     * @param a Author to add to the database in Json format from the request body
     * @return {@link Author} copy of the newly saved author if succeeded
     */
    @PostMapping(consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Author addNewAuthor(@RequestBody Author a){
        return authorService.addNewAuthor(a);
    }

    /**
     * DELETE Mapping for Author
     * <p>
     * Allows to delete an existing author based on the author ID
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * An author may be deleter under {@code /authors/012345} where the number in the path
     * is the ID of the author to delete
     * </dd>
     * </dl>
     *
     * @param authID path variable ID of the author to delete
     */
    @DeleteMapping("/{authID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long authID){
        authorService.deleteAuthor(authID);
    }

    /**
     * PUT Mapping for Author
     * <p>
     * Allows to update an existing author's details using its corresponding author ID
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * An author may be updated under the path {@code /authors/012345} where the number in the path
     * is the ID of the author to update. A request body containing the same ID must be present as well!
     * </dd>
     * </dl>
     *
     * @param authID path variable ID of the existing author to update
     * @param a Json formatted author in the request body with the updated details containing the author ID
     * @return {@link Author} copy of the saved and updated author if succeeded
     */
    @PutMapping("/{authID}")
    public Author updateBook(@PathVariable Long authID, @RequestBody Author a){
        return authorService.updateAuthor(authID,a);
    }
}
