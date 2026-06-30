package app.repository;

import app.model.Stable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StableRepository extends JpaRepository<Stable, Integer> {
    Optional<Stable> findByName(String name);
    boolean existsByName(String name);
}
