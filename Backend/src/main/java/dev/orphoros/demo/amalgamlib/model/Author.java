package dev.orphoros.demo.amalgamlib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Author Entity Model
 * <p>
 * This class is responsible for modeling the author entity and providing getters and setters.
 * <p>
 * All parameters of the author model are mandatory, thus none of them should be null once saving the entity to
 * the database! No primitive types can be used, so it is possible to check whether the value is given or not, thus
 * default value can be avoided.
 *
 * <dl>
 * <dt><span class="strong">Implementation Note:</span></dt>
 * <dd>
 * The author model should be edited using setters. It is not possible to populate
 * parameters of the author object using a constructor
 * </dd>
 * </dl>
 *
 * @author Orphoros
 * @version 1.0
 * @see dev.orphoros.demo.amalgamlib.controllers.AuthorController AutherController
 * @see dev.orphoros.demo.amalgamlib.services.AuthorService AuthorService
 * @see dev.orphoros.demo.amalgamlib.repository.AuthorRepository AuthorRepository
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Author {

    /**
     * ID of the author (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the author
     */
    @Column
    private String name;

    /**
     * Gender of the author
     */
    @Column
    private Boolean male;

    /**
     * Books of the author (Ignored by Json)
     */
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Book> books;

    //==========[GETTERS]==========

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getMale() {
        return male;
    }

    public List<Book> getBooks() {
        return books;
    }

    //==========[SETTERS]==========


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }
}
