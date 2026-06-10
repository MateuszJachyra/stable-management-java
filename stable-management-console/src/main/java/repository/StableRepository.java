package repository;

import model.Horse;
import model.Stable;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

public class StableRepository {
    public void save(EntityManager em, Stable stable){
        em.persist(stable);
    }

    public Optional<Stable> getById(EntityManager em, int id) {
        return Optional.ofNullable(em.find(Stable.class, id));
    }

    public Optional<Stable> findByName(EntityManager em, String name) {
        try {
            Stable stable = em.createQuery("SELECT s FROM Stable s WHERE s.name = :name", Stable.class)
                    .setParameter("name",name).getSingleResult();
            return Optional.ofNullable(stable);
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Horse> getHorsesInStable(EntityManager em, int stableId) {
        return em.createQuery(
                        "SELECT h FROM Horse h LEFT JOIN FETCH h.ratings WHERE h.stable.id = :id",
                        Horse.class)
                .setParameter("id", stableId)
                .getResultList();
    }

    public List<Stable> findAll(EntityManager em) {
        return em.createQuery("SELECT s FROM Stable s LEFT JOIN FETCH s.horses", Stable.class).getResultList();
    }

    public void delete(EntityManager em, Stable stable) {
        em.remove(stable);
    }
}
