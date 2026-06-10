package repository;

import model.Horse;
import model.Rating;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class HorseRepository {

    public int add(EntityManager em, Horse horse) {
        em.persist(horse);
        em.flush();
        return horse.getId();
    }

    public Optional<Horse> findById(EntityManager em, int id) {
        return Optional.ofNullable(em.find(Horse.class, id));
    }

    public void addRating(EntityManager em, int horseId, int rating) {
        Horse horse = em.find(Horse.class,horseId);
        Rating newRating = new Rating(rating,horse);
        horse.getRatings().add(newRating);
        em.persist(horse);
    }

    public void delete(EntityManager em, Horse horse) {
        em.remove(horse);
    }
}
