package model;

import jakarta.persistence.*;

@Entity
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rating_id", unique=true, nullable=false)
    private int id;
    @Column(name="rating", nullable=false)
    private int rating;

    @ManyToOne
    @JoinColumn(name="horse_id", nullable=false)
    private Horse horse;

    public Rating(int rating, Horse horse) {
        this.rating = rating;
        this.horse = horse;
    }

    protected Rating() {}

    @Override
    public String toString() {
        return String.valueOf(rating);
    }

    public Horse getHorse() {
        return horse;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }
}
