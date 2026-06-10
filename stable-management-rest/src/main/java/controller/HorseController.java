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

    @GetMapping("/{id}")
    public ResponseEntity<HorseResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(stableService.findHorseById(id));
    }

    @PostMapping
    public ResponseEntity<HorseResponseDTO> addHorse(@Valid @RequestBody HorseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stableService.addHorse(dto.getStableName(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHorse(@PathVariable int id) {
        stableService.deleteHorse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<List<RatingResponseDTO>> getRating(@PathVariable int id) {
        return ResponseEntity.ok(stableService.getRatingsByHorseId(id));
    }

    @PostMapping("/rating")
    public ResponseEntity<RatingResponseDTO> addRating(@Valid @RequestBody RatingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stableService.addRating(dto.getHorseId(),dto.getValue()));
    }
}
