package model;

import jakarta.persistence.*;

@Entity
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rating_id", unique=true, nullable=false)
    private int id;
    @Column(name="value", nullable=false)
    private int value;

    @Column(name="comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name="horse_id", nullable=false)
    private Horse horse;

    public Rating(int value, Horse horse,  String comment) {
        this.value = value;
        this.horse = horse;
        this.comment = comment;
    }

    protected Rating() {}

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public Horse getHorse() {
        return horse;
    }

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
