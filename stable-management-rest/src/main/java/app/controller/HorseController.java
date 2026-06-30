package app.controller;

import app.dto.HorseDTO;
import app.dto.HorseResponseDTO;
import app.dto.RatingDTO;
import app.dto.RatingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.service.StableService;

import java.util.List;

@RestController
@RequestMapping("/api/horses")
@Tag(name = "Horse Management", description = "Endpoints for managing horses and their ratings")
public class HorseController {
    private final StableService stableService;

    public HorseController(StableService stableService) {
        this.stableService = stableService;
    }

    @Operation(summary = "Get all horses", description = "Retrieve a list of all horses in the database")
    @GetMapping
    public ResponseEntity<List<HorseResponseDTO>> getAll() {
        return ResponseEntity.ok(stableService.getAllHorses());
    }

    @Operation(summary="Get horse by ID", description = "Retrieve a horse by its unique ID")
    @GetMapping("/{horseId}")
    public ResponseEntity<HorseResponseDTO> getById(@PathVariable int horseId) {
        return ResponseEntity.ok(stableService.findHorseById(horseId));
    }

    @Operation(summary="Add a horse", description = "Add a new horse to a stable")
    @PostMapping
    public ResponseEntity<HorseResponseDTO> addHorse(@Valid @RequestBody HorseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stableService.addHorse(dto.getStableName(), dto));
    }

    @Operation(summary="Update horse", description = "Update an existing horse's details")
    @PutMapping("/{horseId}")
    public ResponseEntity<HorseResponseDTO> updateHorse(@PathVariable int horseId,
                                                        @Valid @RequestBody HorseDTO dto) {
        return ResponseEntity.ok(stableService.updateHorse(horseId, dto));
    }

    @Operation(summary="Delete a horse from database", description = "Delete a horse by its unique ID")
    @DeleteMapping("/{horseId}")
    public ResponseEntity<Void> deleteHorse(@PathVariable int horseId) {
        stableService.deleteHorse(horseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get horse's ratings", description = "Retrieve all ratings for a specific " +
            "horse by its unique ID")
    @GetMapping("/{horseId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable int horseId) {
        return ResponseEntity.ok(stableService.getRatingsByHorseId(horseId));
    }

    @Operation(summary = "Add a rating to a horse", description = "Add a new rating for a specific " +
            "horse by its unique ID")
    @PostMapping("/{horseId}/ratings")
    public ResponseEntity<RatingResponseDTO> addRating(@PathVariable int horseId,
                                                       @Valid @RequestBody RatingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                stableService.addRating(horseId, dto.getValue(), dto.getComment()));
    }

    @Operation(summary = "Update horse's rating", description = "Update an existing rating for a specific " +
            "horse by its unique ID and the rating's unique ID")
    @PutMapping("/{horseId}/ratings/{ratingId}")
    public ResponseEntity<RatingResponseDTO> updateRating(@PathVariable int horseId,
                                                          @PathVariable int ratingId,
                                                          @Valid @RequestBody RatingDTO dto) {
        return ResponseEntity.ok(stableService.updateRating(horseId, ratingId, dto));
    }

    @Operation(summary = "Delete horse's rating", description = "Delete a specific rating for a specific " +
            "horse by its unique ID and the rating's unique ID")
    @DeleteMapping("/{horseId}/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable int horseId, @PathVariable int ratingId) {
        stableService.deleteRating(horseId, ratingId);
        return ResponseEntity.noContent().build();
    }
}
