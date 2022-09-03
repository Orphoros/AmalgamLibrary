package dev.orphoros.demo.amalgamlib.services;

import dev.orphoros.demo.amalgamlib.model.Review;
import dev.orphoros.demo.amalgamlib.repository.BookRepository;
import dev.orphoros.demo.amalgamlib.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import dev.orphoros.demo.amalgamlib.exceptions.*;

import java.util.List;
import java.util.Optional;

/**
 * Review Service
 * <p>
 * This class is responsible validating parameters, checking for exceptions and connecting the
 * {@code ReviewController} with the {@code ReviewRepository}.
 * <p>
 * This class may throw HTTP status codes in the 400 range if an exception happened.
 * <p>
 * HTTP status codes in the 500 range may be thrown as well if an uncaught exception happens.
 *
 * @author Orphoros
 * @version 1.2
 * @see dev.orphoros.demo.amalgamlib.controllers.ReviewController ReviewController
 * @see dev.orphoros.demo.amalgamlib.services.ReviewService ReviewService
 * @see dev.orphoros.demo.amalgamlib.repository.ReviewRepository ReviewRepository
 */
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    /**
     * Main constructor of the {@code ReviewService} class.
     * @param reviewRepository Repository that belongs to Review
     * @param bookRepository Repository that belongs to review's parent book
     */
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Gives a list of filtered reviews based on filter parameters
     * <p>
     * Some parameters are optional for filtering
     * @param ISBN The book to which the review has been written (must not be null)
     * @param stars Book's rating with stars
     * @param nickname Name of the reviewer (case sensitive)
     * @return {@link List} of found reviews
     * @throws BadRequestException if parameter validation fails
     */
    public List<Review> getFilteredReviews(Long ISBN, Integer stars, String nickname){
        if(ISBN == null) throw new BadRequestException("ISBN is not provided!");

        //No param is given
        if(stars == null && nickname == null) return reviewRepository.findAllByBookISBN(ISBN);
        //Both params are given
        if(stars != null && nickname != null) return getReviewsByStarsAndNickname(ISBN, stars, nickname);
        //Only stars are given
        if(stars != null) return getReviewsByStarsGiven(ISBN, stars);
        //Only nickname is given
        return getReviewsByWriterNickname(ISBN, nickname);
    }

    /**
     * Finds a review by its corresponding ID
     * @param id ID of the review to find
     * @return {@link Review} that belongs to the ID
     * @throws NotFoundException if the ID does not exist in the database
     * @throws BadRequestException if the ID is empty is incorrect
     */
    public Review getReviewByID (Long id){
        validateID(id);
        Optional<Review> r = reviewRepository.findByReviewID(id);
        if(r.isPresent()) return r.get();
        throw new NotFoundException("Could not find any review by id of " + id + "!");
    }

    /**
     * Save a new review to the database
     * @param r Review object to save
     * @return {@link Review} object that has been successfully saved
     * @throws BadRequestException if the review object is not properly formatted
     * @throws NotFoundException if the review's parent Book ISBN could not be found
     */
    public Review addNewReview (Review r) {
        validateReview(r);
        if(!bookRepository.existsByISBN(r.getBook().getISBN())) throw new NotFoundException("Review's parent book ISBN could not be found!");
        if(r.getReviewID() != null) throw new BadRequestException("Review ID should not be defined!");
        return reviewRepository.save(r);
    }

    /**
     * Delete an existing review from the database
     * @param id ID of the existing review to be deleted
     * @throws BadRequestException if the ID is not properly formatted
     * @throws NotFoundException if the ID does not exist in the database
     */
    public void deleteReview(Long id){
        validateID(id);
        if(!reviewRepository.existsByReviewID(id)) throw new NotFoundException("Review ID does not exist in the database!");
        reviewRepository.deleteById(id);
    }

    /**
     * Update the values of an existing review in the database
     * @param id ID of the existing review object
     * @param r Review object with the updated values (ID must be present as well)
     * @return {@link Review} object with the new values that has been successfully updated
     * @throws BadRequestException if the author object is not properly formatted or the IDs are incorrect
     * @throws NotFoundException if the IDs do not exist in the database
     */
    public Review updateReview(Long id, Review r){
        if(r.getReviewID() == null) throw new BadRequestException("Review's ID is not specified!");
        if(r.getBook() != null) throw new BadRequestException("Review's book ISBN cannot be changed once assigned!");
        validateReview(r);
        validateID(id);
        if(!id.equals(r.getReviewID())) throw new BadRequestException("Review IDs do not match!");

        return reviewRepository.findById(id)
                .map(review -> {
                    review.setComment(r.getComment());
                    review.setNickname(r.getNickname());
                    review.setStarsGiven(r.getStarsGiven());
                    return reviewRepository.save(review);
                }).orElseThrow(() -> new NotFoundException("Review ID not found!"));
    }

    //=========================[Helper methods]=========================

    /**
     * Validates a review object's properties
     * @param r Review object to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateReview(Review r){
        if(r.getBook() == null || r.getBook().getISBN() == null) throw new BadRequestException("Parent book ISBN must be given in the request body!");
        if(r.getComment() == null || r.getComment().isEmpty()) throw new BadRequestException("Review comment is not provided");
        if(r.getBook().getAuthor() != null || r.getBook().getReviews() != null || r.getBook().getForKids() != null || r.getBook().getPublishYear() != null || r.getBook().getTitle() != null )
            throw new BadRequestException("Book parameters cannot be given here");
        validateStars(r.getStarsGiven());
        validateNickname(r.getNickname());
    }

    /**
     * Validates the format of an ID
     * @param id ID to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateID(Long id){
        if(id == null) throw new BadRequestException("Review ID is not defined!");
        if(id < 0) throw new BadRequestException("Review ID must be a positive number!");
    }

    /**
     * Validates the format of the Stars parameter
     * @param stars Stars to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateStars(Integer stars){
        if(stars < 1 || stars > 5) throw new BadRequestException("Stars must be between 1 and 5! " + stars + " starts is invalid!");
    }

    /**
     * Validates the format of the Nickname parameter
     * @param nickname Nickname to be validated
     * @throws BadRequestException if validation failed
     */
    private void validateNickname(String nickname){
        if(nickname == null || nickname.isEmpty()) throw new BadRequestException("Nickname cannot be empty!");
    }

    // =========================[FILTERING]============================

    /**
     * Gives results based on filter parameters.
     * @param ISBN Book ID
     * @param stars Rated stars
     * @param nickname Nickname of the author of review
     * @return {@link List} of found reviews. An empty list is returned if nothing is found.
     */
    private List<Review> getReviewsByStarsAndNickname(Long ISBN, Integer stars, String nickname){
        return reviewRepository.findAllByBookISBNAndStarsGivenAndNickname(ISBN, stars, nickname);
    }

    /**
     * Gives results based on filter parameters.
     * @param ISBN Book ID
     * @param stars Rated stars
     * @return {@link List} of found reviews. An empty list is returned if nothing is found.
     */
    private List<Review> getReviewsByStarsGiven(Long ISBN, Integer stars){
        return reviewRepository.findByBookISBNAndStarsGiven(ISBN, stars);
    }

    /**
     * Gives results based on filter parameters.
     * @param ISBN Book ID
     * @param nickname Nickname of the author of review
     * @return {@link List} of found reviews. An empty list is returned if nothing is found.
     */
    private List<Review> getReviewsByWriterNickname(Long ISBN, String nickname){
        return reviewRepository.findAllByBookISBNAndNickname(ISBN, nickname);
    }
}
