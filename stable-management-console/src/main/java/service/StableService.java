package service;

import config.JpaExecutor;
import exception.HorseOperationException;
import exception.StableOperationException;
import exception.StableServiceOperationException;
import model.Horse;
import model.Stable;
import repository.HorseRepository;
import repository.StableRepository;

import java.util.*;

public class StableService {
    private final StableRepository stableRepository = new StableRepository();
    private final HorseRepository horseRepository = new HorseRepository();

    public void createStable(String name, int capacity) throws StableServiceOperationException {
        if(capacity <= 0){
            throw new StableServiceOperationException("Capacity must be greater than 0");
        }
        Stable stable = new Stable(name, capacity);

        JpaExecutor.executeInTransactionVoid(em ->{
            if(stableRepository.findByName(em, name).isPresent()) {
                throw new StableServiceOperationException("Stable already exists");
            }
            stableRepository.save(em, stable);
        });
    }

    public void deleteStable(String name) throws StableServiceOperationException, StableOperationException {
        JpaExecutor.executeInTransactionVoid(em ->{
            Optional<Stable> stable = stableRepository.findByName(em, name);
            if(stable.isEmpty()) {
                throw new StableServiceOperationException("Stable not found");
            }
            if(!stableRepository.getHorsesInStable(em,stable.get().getId()).isEmpty()) {
                throw new StableOperationException("Stable is not empty");
            }
            stableRepository.delete(em, stable.get());
        });
    }

    public void forceDeleteStable(String name) {
        JpaExecutor.executeInTransactionVoid(em -> {
            Optional<Stable> stable = stableRepository.findByName(em, name);
            if(stable.isEmpty()) {
                throw new StableServiceOperationException("Stable not found");
            }
            else {
                List<Horse> horses = stable.get().getHorses();
                for (Horse horse : horses) {
                    stable.get().deleteHorse(horse.getId());
                }
                stableRepository.delete(em, stable.get());
            }
        });
    }

    public List<Stable> listStables() throws StableServiceOperationException {
        return JpaExecutor.execute(stableRepository::findAll);
    }

    public Stable getStable(String name) throws StableServiceOperationException {
        return JpaExecutor.execute(em -> {
            Optional<Stable> stable = stableRepository.findByName(em, name);
            if (stable.isEmpty()) {
                throw new StableServiceOperationException("Stable not found");
            }
            return stable.get();
        });
    }

    public List<Horse> getHorsesSorted(String stableName)  throws StableServiceOperationException {
        return JpaExecutor.executeInTransaction(em -> {
            Optional<Stable> stable = stableRepository.findByName(em,stableName);
            if(stable.isEmpty()) {
                throw new StableServiceOperationException("Stable not found");
            }
            else {
                List<Horse> horses = stableRepository.getHorsesInStable(em, stable.get().getId());
                Collections.sort(horses);
                return horses;
            }
        });
    }

    public int addHorse(String stableName, Horse horse) throws StableServiceOperationException {
        return JpaExecutor.executeInTransaction(em -> {
            Optional<Stable> stable = stableRepository.findByName(em,stableName);
            if (stable.isEmpty()) {
                throw new StableServiceOperationException("Horse stable not found");
            }
            horse.setStable(stable.get());
            stable.get().addHorse(horse);
            return horseRepository.add(em, horse);
        });
    }

    public void deleteHorse(int id) throws StableServiceOperationException {
        JpaExecutor.executeInTransactionVoid(em -> {
            Optional<Horse> horse = horseRepository.findById(em,id);
            if(horse.isEmpty()){
                throw new StableServiceOperationException("Horse not found");
            }
            else {
                horseRepository.delete(em,horse.get());
            }
        });
    }

    public Horse findHorseById(int id) throws StableServiceOperationException {
        return JpaExecutor.execute(em -> {
            Optional<Horse> horse = horseRepository.findById(em, id);
            if (horse.isEmpty()) {
                throw new StableServiceOperationException("Horse not found");
            }
            else {
                return horse.get();
            }
        });
    }

    public void addRating(int id, int rating) throws  HorseOperationException {
        if (rating > 5 || rating < 1) {
            throw new HorseOperationException("Invalid rating");
        }
        JpaExecutor.executeInTransactionVoid(em -> {
            horseRepository.addRating(em,id,rating);
        });
    }


}
