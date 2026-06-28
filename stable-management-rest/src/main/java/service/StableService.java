package service;

import dto.HorseDTO;
import dto.HorseResponseDTO;
import dto.RatingDTO;
import dto.RatingResponseDTO;
import dto.StableDTO;
import dto.StableResponseDTO;
import exception.HorseOperationException;
import exception.StableOperationException;
import exception.StableServiceOperationException;
import model.Horse;
import model.Rating;
import model.Stable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.HorseRepository;
import repository.RatingRepository;
import repository.StableRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class StableService {
    private final StableRepository stableRepository;
    private final HorseRepository horseRepository;
    private final RatingRepository ratingRepository;

    public StableService(StableRepository stableRepository, HorseRepository horseRepository,
                         RatingRepository ratingRepository) {
        this.stableRepository = stableRepository;
        this.horseRepository = horseRepository;
        this.ratingRepository = ratingRepository;
    }

    public StableResponseDTO createStable(String name, int capacity) throws StableServiceOperationException {
        if(capacity <= 0){
            throw new StableServiceOperationException("Capacity must be greater than 0");
        }
        if(stableRepository.existsByName(name)){
            throw new StableServiceOperationException("Stable name already exists");
        }
        Stable stable = new Stable(name, capacity);

        stableRepository.save(stable);
        stable = stableRepository.findByName(name)
                .orElseThrow(() -> new StableServiceOperationException("Stable not found"));
        return StableResponseDTO.from(stable);
    }

    public StableResponseDTO updateStable(String name, StableDTO dto) {
        Stable stable = stableRepository.findByName(name)
                .orElseThrow(() -> new StableServiceOperationException("Stable not found"));

        if (!name.equals(dto.getName()) && stableRepository.existsByName(dto.getName())) {
            throw new StableServiceOperationException("Stable name already exists");
        }
        stable.setName(dto.getName());
        stable.setMaxCapacity(dto.getCapacity());

        Stable updatedStable = stableRepository.save(stable);
        return StableResponseDTO.from(updatedStable);
    }

    public void deleteStable(String name, boolean force) throws StableServiceOperationException {
        if(force){
            forceDeleteStable(name);
        }
        else {
            Stable stable = stableRepository.findByName(name)
                    .orElseThrow(() -> new StableServiceOperationException("Stable not found"));
            if(!horseRepository.findStableIdWithRatings(stable.getId()).isEmpty()){
                throw new StableServiceOperationException("Stable not empty");
            }
                stableRepository.delete(stable);
        }
    }

    public void forceDeleteStable(String name) throws StableServiceOperationException {
        Stable stable = stableRepository.findByName(name)
                .orElseThrow(() -> new StableServiceOperationException("Stable not found"));
        List<Horse> horses = stable.getHorses();
        horseRepository.deleteAll(horses);
        stableRepository.delete(stable);
    }

    public List<StableResponseDTO> listStables() {
        List<Stable> stables = stableRepository.findAll();
        List<StableResponseDTO> stablesDTO = new ArrayList<>();
        for (Stable stable : stables) {
            stablesDTO.add(StableResponseDTO.from(stable));
        }
        return stablesDTO;
    }

    public StableResponseDTO getStable(String name) throws StableServiceOperationException {
        Stable stable = stableRepository.findByName(name)
                .orElseThrow(() -> new StableServiceOperationException("Stable not found"));
        return StableResponseDTO.from(stable);
    }

    public List<HorseResponseDTO> getHorsesSorted(String stableName) throws StableServiceOperationException {
        Stable stable = stableRepository.findByName(stableName)
                .orElseThrow(() -> new StableServiceOperationException("Stable not found"));
        List<Horse> horses = horseRepository.findStableIdWithRatings(stable.getId());
        Collections.sort(horses);
        List<HorseResponseDTO> horsesDTO = new ArrayList<>();
        for( Horse horse : horses ){
            horsesDTO.add(HorseResponseDTO.from(horse));
        }
        return horsesDTO;
    }

    public List<HorseResponseDTO> getAllHorses() {
        List<Horse> horses = horseRepository.findAll();
        List<HorseResponseDTO> horsesDTO = new ArrayList<>();
        for (Horse horse : horses) {
            horsesDTO.add(HorseResponseDTO.from(horse));
        }
        return horsesDTO;
    }

    public HorseResponseDTO addHorse(String stableName, HorseDTO dto)
            throws StableServiceOperationException, StableOperationException {
        Horse horse = new Horse(
                dto.getName(), dto.getBreed(), dto.getType(), dto.getStatus(),
                dto.getAge(), dto.getPrice(), dto.getWeight()
        );
        Stable stable = stableRepository.findByName(stableName)
                .orElseThrow(() -> new StableServiceOperationException("Stable not found"));
        horse.setStable(stable);
        stable.addHorse(horse);
        horseRepository.save(horse);
        return HorseResponseDTO.from(horse);
    }

    public HorseResponseDTO updateHorse(int horseId, HorseDTO dto)
            throws StableServiceOperationException, StableOperationException {
        Horse horse = horseRepository.findById(horseId)
                .orElseThrow(() -> new StableServiceOperationException("Horse not found"));

        horse.setName(dto.getName());
        horse.setBreed(dto.getBreed());
        horse.setType(dto.getType());
        horse.setStatus(dto.getStatus());
        horse.setAge(dto.getAge());
        horse.setPrice(dto.getPrice());
        horse.setWeight(dto.getWeight());

        Stable currentStable = horse.getStable();
        if (currentStable == null || !currentStable.getName().equals(dto.getStableName())) {
            Stable newStable = stableRepository.findByName(dto.getStableName())
                    .orElseThrow(() -> new StableServiceOperationException(
                            "Target stable not found: " + dto.getStableName()));

            if (newStable.isFull()) {
                throw new StableOperationException("Target stable is full");
            }
            horse.setStable(newStable);
        }
        return HorseResponseDTO.from(horseRepository.save(horse));
    }

    public void deleteHorse(int id) throws StableServiceOperationException {
        if(!horseRepository.existsById(id)){
            throw new StableServiceOperationException("Horse not found");
        }
        horseRepository.deleteById(id);
    }

    public HorseResponseDTO findHorseById(int id) throws StableServiceOperationException {
        return HorseResponseDTO.from(horseRepository.findById(id)
                .orElseThrow(() -> new StableServiceOperationException("Horse not found")));
    }

    public RatingResponseDTO addRating(int horseId, int value, String comment) throws HorseOperationException {
        if (value > 5 || value < 1) {
            throw new HorseOperationException("Invalid rating");
        }
        Horse horse = horseRepository.findById(horseId)
                .orElseThrow(() -> new HorseOperationException("Horse not found"));
        Rating newRating = new Rating(value, horse, comment);
        horse.getRatings().add(newRating);
        horseRepository.save(horse);
        return RatingResponseDTO.from(newRating);
    }

    public RatingResponseDTO updateRating(int horseId, int ratingId, RatingDTO dto) throws HorseOperationException {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new HorseOperationException("Rating not found"));

        if (rating.getHorse().getId() != horseId) {
            throw new HorseOperationException("Rating does not belong to the specified horse");
        }

        rating.setValue(dto.getValue());
        rating.setComment(dto.getComment());
        Rating updatedRating = ratingRepository.save(rating);
        return RatingResponseDTO.from(updatedRating);
    }

    public void deleteRating(int horseId, int ratingId) throws HorseOperationException {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new HorseOperationException("Rating not found"));

        if (rating.getHorse().getId() != horseId) {
            throw new HorseOperationException("Rating does not belong to the specified horse");
        }

        ratingRepository.delete(rating);
    }

    public List<RatingResponseDTO> getRatingsByHorseId(int id) throws StableServiceOperationException {
        Horse horse = horseRepository.findById(id)
                .orElseThrow(() -> new StableServiceOperationException("Horse not found"));
        List<RatingResponseDTO> ratingsDTO = new ArrayList<>();
        horse.getRatings().forEach(rating -> {ratingsDTO.add(RatingResponseDTO.from(rating));});
        return ratingsDTO;
    }
}
