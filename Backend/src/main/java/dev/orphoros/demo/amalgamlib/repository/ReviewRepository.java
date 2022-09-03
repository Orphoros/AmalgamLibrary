package dev.orphoros.demo.amalgamlib.repository;

import dev.orphoros.demo.amalgamlib.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Review Repository
 * <p>
 * This class is responsible for communicating with the external database with review data
 * <p>
 * Each function in this class is a custom defined command for the
 * {@link dev.orphoros.demo.amalgamlib.services.ReviewService ReviewService} class to issue
 * SQL commands to the database. Updates and changes in this class must/should be represented in the {@code ReviewService}
 * class respectively.
 *
 * @author Orphoros
 * @version 1.1
 * @see Review Review
 * @see dev.orphoros.demo.amalgamlib.services.ReviewService ReviewService
 * @see dev.orphoros.demo.amalgamlib.controllers.ReviewController ReviewController
 */
@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    /**
     * Find every single review in the database that belongs to a book and return them in a list
     * @param ISBN Book ID
     * @return {@link List} of found reviews
     */
    List<Review> findAllByBookISBN(Long ISBN);

    /**
     * Find every review in the database by its corresponding book and rated stars at the same time
     * @param ISBN Book ID
     * @param givenStars Rated stars of the review
     * @return {@link List} of found reviews
     */
    List<Review> findByBookISBNAndStarsGiven(Long ISBN, Integer givenStars);

    /**
     * Find every review in the database by its corresponding book and reviewer's nickname at the same time
     * @param ISBN Book ID
     * @param nickname Exact nickname of the review to find (Case sensitive)
     * @return {@link List} of found reviews
     */
    List<Review> findAllByBookISBNAndNickname(Long ISBN, String nickname);

    /**
     * Find every review in the database by its corresponding book, rated stars, and reviewer's nickname at the same time
     * @param ISBN Book ID
     * @param givenStars Rated stars of the review
     * @param nickname Exact nickname of the review to find (Case sensitive)
     * @return {@link List} of found reviews
     */
    List<Review> findAllByBookISBNAndStarsGivenAndNickname(Long ISBN, Integer givenStars, String nickname);

    /**
     * Find a specific review by its ID
     * @param id ID of the comment to find. Should not be null!
     * @return {@link Optional} Review
     */
    Optional<Review> findByReviewID(Long id);

    /**
     * Check if a review exists in the database or not
     * @param id ID of the comment to find. Should not be null!
     * @return True if review exists
     */
    boolean existsByReviewID(Long id);
}
