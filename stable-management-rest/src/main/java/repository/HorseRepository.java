package repository;

import model.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorseRepository extends JpaRepository<Horse, Integer> {
    @Query("SELECT h FROM Horse h LEFT JOIN FETCH h.ratings WHERE h.stable.id = :stableId")
    List<Horse> findStableIdWithRatings(@Param("stableId") Integer stableId);
}
