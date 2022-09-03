package dev.orphoros.demo.amalgamlib.services;

import dev.orphoros.demo.amalgamlib.exceptions.*;
import dev.orphoros.demo.amalgamlib.model.Author;
import dev.orphoros.demo.amalgamlib.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Author Service
 * <p>
 * This class is responsible validating parameters, checking for exceptions and connecting the
 * {@code AuthorController} with the {@code AuthorRepository}.
 * <p>
 * This class may throw HTTP status codes in the 400 range if an exception happened.
 * <p>
 * HTTP status codes in the 500 range may be thrown as well if an uncaught exception happens.
 *
 * @author Orphoros
 * @version 1.1
 * @see dev.orphoros.demo.amalgamlib.controllers.AuthorController AutherController
 * @see dev.orphoros.demo.amalgamlib.services.AuthorService AuthorService
 * @see dev.orphoros.demo.amalgamlib.repository.AuthorRepository AuthorRepository
 */
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    /**
     * Main constructor of the {@code AuthorService} class.
     * @param authorRepository Repository that belongs to Author
     */
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Gives a list of filtered authors based on filter parameters.
     * <p>
     * All parameters are optional for filtering
     * @param male Gender of the author
     * @param name Keyword to be found in the name of the author (case sensitive)
     * @return {@link List} of found authors
     * @throws BadRequestException if the name is not null but empty
     */
    public List<Author> getFilteredAuthors (Boolean male, String name){
        if(name != null && name.isEmpty()) throw new BadRequestException("Name cannot be empty!");

        //No param is given
        if(male == null && name == null) return authorRepository.findAll();
        //All param is given
        if(male != null && name != null) return authorRepository.findAuthorsByMaleAndNameContaining(male, name);
        //Male param is given
        if(male != null) return authorRepository.findAuthorsByMale(male);
        //Name param is given
        return authorRepository.findAuthorsByNameContaining(name);
    }

    /**
     * Finds an author by their corresponding ID
     * @param id ID of the author to find
     * @return {@link Author} who belongs to the ID
     * @throws NotFoundException if the ID does not exist in the database
     * @throws BadRequestException if the ID is not properly formatted
     */
    public Author getAuthorByID(Long id){
        validateID(id);
        Optional<Author> a = authorRepository.findById(id);
        if(a.isPresent()) return a.get();
        else throw new NotFoundException("Could not find any author by the ID of " + id + "!");
    }

    /**
     * Save a new author to the database
     * @param a Author object to save
     * @return {@link Author} object that has been successfully saved
     * @throws BadRequestException if the author object is not properly formatted
     */
    public Author addNewAuthor(Author a){
        if(a.getId() != null) throw new BadRequestException("Author ID should not be defined as it is automatically generated!");
        validateAuthor(a);
        return authorRepository.save(a);
    }

    /**
     * Delete an existing author from the database
     * @param id ID of the existing author to be deleted
     * @throws BadRequestException if the ID is not properly formatted
     * @throws NotFoundException if the author does not exist in the database
     */
    public void deleteAuthor(Long id){
        validateID(id);
        if(!authorRepository.existsAuthorById(id)) throw new NotFoundException("Author under ID " + id + " does not exist in the database!");
        authorRepository.deleteById(id);
    }

    /**
     * Update the values of an existing author in the database
     * @param id ID of the existing author object
     * @param a Author object with the updated values (ID must be present as well)
     * @return {@link Author} object with the new values that has been successfully updated
     * @throws BadRequestException if the author object is not properly formatted or the IDs are incorrect
     * @throws NotFoundException if the ID does not exist in the database
     */
    public Author updateAuthor(Long id, Author a){
        validateAuthor(a);
        validateID(id);
        if(a.getId() == null) throw new BadRequestException("Author ID is not defined in request body!");
        if(!a.getId().equals(id)) throw new BadRequestException("Author IDs do not match!");

        //Update author
        return authorRepository.findById(id)
                .map(auth -> {
                    auth.setMale(a.getMale());
                    auth.setName(a.getName());
                    return authorRepository.save(auth);
                }).orElseThrow(() -> new NotFoundException("Author ID could not be found!"));
    }

    //=========================[Helper methods]=========================

    /**
     * Validates an author object's properties
     * @param a Author object to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateAuthor(Author a){
        if(a.getName() == null || a.getName().isEmpty()) throw new BadRequestException("Author name was not given!");
        if(a.getMale() == null) throw new BadRequestException("Author gender must be defined!");
        if(a.getBooks() != null) throw new BadRequestException("Author books cannot be given here!");
    }

    /**
     * Validates the format of an ID
     * @param id ID to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateID(Long id){
        if(id == null || id < 0) throw new BadRequestException("ID value is not valid as it must be an existing positive value!");
    }
}
