package model;

import enumeration.HorseStatus;
import enumeration.HorseType;
import exception.HorseOperationException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="horses")
public class Horse implements Comparable<Horse> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="horse_id", nullable=false, unique=true)
    private int id;
    @Column(name="name", nullable=false, length=50)
    private String name;
    @Column(name="breed", nullable=false, length=50)
    private String breed;
    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable=false)
    private HorseType type;
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private HorseStatus status;
    @Column(name="age", nullable=false)
    private int age;
    @Column(name="price", nullable=false)
    private double price;
    @Column(name="weight", nullable=false)
    private double weight;
    @ManyToOne
    @JoinColumn(name="stable_id", nullable=false)
    private Stable stable;
    @OneToMany(mappedBy="horse",orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    public Horse(String name, String breed, HorseType type, HorseStatus status, int age, double price, double weight) {
        this.name = name;
        this.breed = breed;
        this.type = type;
        this.status = status;
        this.age = age;
        this.price = price;
        this.weight = weight;
    }

    protected Horse() {}

    public double getAverageRating() {
        if(ratings.isEmpty()) {
            return 0.0;
        }
        double averageRating = 0.0;
        for(Rating rating : ratings) {
            averageRating += rating.getRating();
        }
        return averageRating / ratings.size();
    }

    @Override
    public int compareTo(Horse horse) {
        return Double.compare(horse.getAverageRating(), this.getAverageRating());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horse horse = (Horse) o;
        return id == horse.id;
    }

    @Override
    public String toString() {
        return "ID: "+id+", Name: "+name+", Breed: "+breed+", Type: "+type+", Status: "+status+", Age: "+age+", Price: "+price+", Weight: "+weight+", AvgRating: "+getAverageRating();
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void addRating(int rating) throws HorseOperationException {
        if(rating>=1 && rating<=5) {
            ratings.add(new Rating(rating,this));
        }
        else {
            throw new HorseOperationException("Invalid rating");
        }
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public HorseStatus getStatus() {
        return status;
    }

    public void setStatus(HorseStatus status) {
        this.status = status;
    }

    public HorseType getType() {
        return type;
    }

    public void setType(HorseType type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stable getStable() {
        return stable;
    }

    public void setStable(Stable stable) {
        this.stable = stable;
    }
}
