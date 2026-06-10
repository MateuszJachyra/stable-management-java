package config;

import jakarta.persistence.*;

public class JpaConfig {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("stablesPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
