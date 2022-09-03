package dev.orphoros.demo.amalgamlib.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Review Entity Model
 * <p>
 * This class is responsible for modeling the review entity and providing getters and setters.
 * <p>
 * All parameters of the review model are mandatory, thus none of them should be null once saving the entity to
 * the database! No primitive types can be used, so it is possible to check whether the value is given or not, thus
 * default value can be avoided.
 *
 * <dl>
 * <dt><span class="strong">Implementation Note:</span></dt>
 * <dd>
 * The review model should be edited using setters. It is not possible to populate
 * parameters of the review object using a constructor
 * </dd>
 * </dl>
 *
 * @author Orphoros
 * @version 1.0
 * @see dev.orphoros.demo.amalgamlib.controllers.ReviewController ReviewController
 * @see dev.orphoros.demo.amalgamlib.services.ReviewService ReviewService
 * @see dev.orphoros.demo.amalgamlib.repository.ReviewRepository ReviewRepository
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review {
    /**
     * ID of the review (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    /**
     * Nickname (name) of the author of the comment
     */
    @Column
    private String nickname;

    /**
     * Stars given for the book
     */
    @Column
    private Integer starsGiven;

    /**
     * Comment given for the book
     */
    @Column
    private String comment;

    /**
     * Book to which the comment belongs (Json back reference)
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_ISBN", nullable = false)
    private Book book;

    //==========[GETTERS]==========

    public Long getReviewID() {
        return reviewID;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getStarsGiven() {
        return starsGiven;
    }

    public String getComment() {
        return comment;
    }

    public Book getBook() {
        return book;
    }

    //==========[GETTERS]==========

    public void setParentedBookISBN(Long ISBN) {
        this.book.setISBN(ISBN);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStarsGiven(Integer starsGiven) {
        this.starsGiven = starsGiven;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
