package dev.orphoros.demo.amalgamlib.controllers;

import dev.orphoros.demo.amalgamlib.model.Review;
import dev.orphoros.demo.amalgamlib.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Review controller
 * <p>
 * This class is responsible for handling GET, PUT, POST and DELETE requests.
 * <p>
 * The review controller is being mapped under the <em>/reviews</em> path. The controller can only
 * work with Json objects
 *
 * @author Orphoros
 * @version 1.2
 * @see Review Review
 * @see ReviewService ReviewService
 * @see dev.orphoros.demo.amalgamlib.repository.ReviewRepository ReviewRepository
 */
@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * Main constructor of the Review Controller
     *
     * @param reviewService for processing requests and checking for exceptions
     */
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * GET Mapping for Review using review ID and ISBN
     * <p>
     * Allows to retrieve from the central database a review by its ID.
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A review may be accessed by calling {@code /reviews/012345} where
     * the number in the path is the ID of the review to retrieve
     * </dd>
     * </dl>
     *
     * @param id path variable ID of the review to retrieve
     * @return {@link Review} by its corresponding ID
     */
    @GetMapping("{id}")
    public Review getReviewByID(@PathVariable Long id){
        return this.reviewService.getReviewByID(id);
    }

    /**
     * GET Mapping for Review
     * <p>
     * Allows to retrieve a list of books based on request parameters. The {@code ISBN} request parameter is mandatory
     * as a different books' reviews cannot be mixed together.
     * @param isbn Mandatory request parameter to filter reviews by corresponding book ISBN
     * @param stars Optional request parameter to filter by stars given as a rating
     * @param nickname Optional request parameter to filter by reviewer's nickname containing a keyword
     * @return {@link List} of found reviews for a book based on the request parameters
     */
    @GetMapping()
    public List<Review> getAllReviews (@RequestParam Long isbn, @RequestParam(required = false) Integer stars, @RequestParam(required = false) String nickname){
        return this.reviewService.getFilteredReviews(isbn, stars, nickname);
    }

    /**
     * POST Mapping for Reviews
     * <p>
     * Allows to add a new Review to the database using Json.
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A new review may be provided in the request body of the POST request in
     * Json format under the path {@code /reviews}.
     * </dd>
     * </dl>
     *
     * @param r Review to add to the database in Json format from the request body
     * @return {@link Review} copy of the newly saved review if succeeded
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNewReview(@RequestBody Review r){
        return this.reviewService.addNewReview(r);
    }

    /**
     * DELETE Mapping for Review
     * <p>
     * Allows to delete an existing review using its ID
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A review may be deleter under {@code /reviews/012345} where the number in the path
     * is the ID of the review to delete
     * </dd>
     * </dl>
     *
     * @param id path variable ID of the review to delete
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id){
        this.reviewService.deleteReview(id);
    }

    /**
     * PUT Mapping for Review
     * <p>
     * Allows to update an existing review's details using its corresponding ID
     *
     * <dl>
     * <dt><span class="strong">Implementation Note:</span></dt>
     * <dd>
     * A review may be updated under the path {@code /books/012345} where the number in the path
     * is the ID of the review to update. A request body containing the same ID must be present as well!
     * </dd>
     * </dl>
     *
     * @param id path variable ID of the existing review to update
     * @param r Json formatted review in the request body with the updated details containing the review ID
     * @return {@link Review} copy of the saved and updated review if succeeded
     */
    @PutMapping("{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review r){
        return this.reviewService.updateReview(id, r);
    }
}
