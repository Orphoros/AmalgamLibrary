package dev.orphoros.demo.amalgamlib.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Book Entity Model
 * <p>
 * This class is responsible for modeling the book entity and providing getters and setters.
 * <p>
 * All parameters of the book model are mandatory, thus none of them should be null once saving the entity to
 * the database! No primitive types can be used, so it is possible to check whether the value is given or not, thus
 * default value can be avoided.
 *
 * <dl>
 * <dt><span class="strong">Implementation Note:</span></dt>
 * <dd>
 * The book model should be edited using setters. It is not possible to populate
 * parameters of the book object using a constructor
 * </dd>
 * </dl>
 *
 * @author Orphoros
 * @version 1.0
 * @see dev.orphoros.demo.amalgamlib.controllers.BookController BookController
 * @see dev.orphoros.demo.amalgamlib.services.BookService BookService
 * @see dev.orphoros.demo.amalgamlib.repository.BookRepository BookRepository
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {
    /**
     * ID of the book (Primary Key)
     */
    @Id
    private Long ISBN;

    /**
     * Title of the book
     */
    @Column
    private String title;

    /**
     * Whether the book is readable by kids or not
     */
    @Column
    private Boolean forKids;

    /**
     * Year when the book was published
     */
    @Column
    private Integer publishYear;

    /**
     * Reviews of the book (Ignored by Json)
     */
    @OneToMany(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Review> reviews;

    /**
     * Author of the book (Json back reference)
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    //==========[GETTERS]==========

    public Long getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getForKids() {
        return forKids;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Author getAuthor() {
        return author;
    }

    //==========[SETTERS]==========

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setForKids(Boolean forKids) {
        this.forKids = forKids;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public void setAuthorID(Long ID) {
        this.author.setId(ID);
    }
}