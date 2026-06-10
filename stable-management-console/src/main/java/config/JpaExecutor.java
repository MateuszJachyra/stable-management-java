package config;

import exception.StableOperationException;
import exception.StableServiceOperationException;
import jakarta.persistence.EntityManager;

import java.util.function.Consumer;
import java.util.function.Function;

public class JpaExecutor {
    public static <T> T executeInTransaction(Function<EntityManager, T> action) throws StableServiceOperationException {
        EntityManager em = JpaConfig.getEntityManager();
        try{
            em.getTransaction().begin();
            T result = action.apply(em);
            em.getTransaction().commit();
            return result;
        }
        catch(StableOperationException e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }
        catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new StableServiceOperationException(e.getMessage(),e);
        }
        finally{
            em.close();
        }
    }

    public static void executeInTransactionVoid(Consumer<EntityManager> action) {
        executeInTransaction(em -> {
            action.accept(em);
            return null;
        });
    }

    public static <T> T execute(Function<EntityManager, T> action) throws StableServiceOperationException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return action.apply(em);
        } catch (Exception e) {
            throw new StableServiceOperationException(e.getMessage(),e);
        }
    }
}
