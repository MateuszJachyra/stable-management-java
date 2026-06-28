package controller;

import dto.HorseDTO;
import dto.HorseResponseDTO;
import dto.RatingDTO;
import dto.RatingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.StableService;

import java.util.List;

@RestController
@RequestMapping("/api/horses")
public class HorseController {
    private final StableService stableService;

    public HorseController(StableService stableService) {
        this.stableService = stableService;
    }

    @GetMapping
    public ResponseEntity<List<HorseResponseDTO>> getAll() {
        return ResponseEntity.ok(stableService.getAllHorses());
    }

    @GetMapping("/{horseId}")
    public ResponseEntity<HorseResponseDTO> getById(@PathVariable int horseId) {
        return ResponseEntity.ok(stableService.findHorseById(horseId));
    }

    @PostMapping
    public ResponseEntity<HorseResponseDTO> addHorse(@Valid @RequestBody HorseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stableService.addHorse(dto.getStableName(), dto));
    }

    @PutMapping("/{horseId}")
    public ResponseEntity<HorseResponseDTO> updateHorse(@PathVariable int horseId,
                                                        @Valid @RequestBody HorseDTO dto) {
        return ResponseEntity.ok(stableService.updateHorse(horseId, dto));
    }

    @DeleteMapping("/{horseId}")
    public ResponseEntity<Void> deleteHorse(@PathVariable int horseId) {
        stableService.deleteHorse(horseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{horseId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable int horseId) {
        return ResponseEntity.ok(stableService.getRatingsByHorseId(horseId));
    }

    @PostMapping("/{horseId}/ratings")
    public ResponseEntity<RatingResponseDTO> addRating(@PathVariable int horseId,
                                                       @Valid @RequestBody RatingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                stableService.addRating(horseId, dto.getValue(), dto.getComment()));
    }

    @PutMapping("/{horseId}/ratings/{ratingId}")
    public ResponseEntity<RatingResponseDTO> updateRating(@PathVariable int horseId,
                                                          @PathVariable int ratingId,
                                                          @Valid @RequestBody RatingDTO dto) {
        return ResponseEntity.ok(stableService.updateRating(horseId, ratingId, dto));
    }

    @DeleteMapping("/{horseId}/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable int horseId, @PathVariable int ratingId) {
        stableService.deleteRating(horseId, ratingId);
        return ResponseEntity.noContent().build();
    }
}
