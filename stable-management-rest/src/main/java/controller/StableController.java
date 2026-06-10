package controller;

import dto.HorseResponseDTO;
import dto.StableDTO;
import dto.StableResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.StableService;

import java.util.List;

@RestController
@RequestMapping("/api/stables")
public class StableController {
    private final StableService stableService;

    public StableController(StableService stableService) {
        this.stableService = stableService;
    }

    @GetMapping
    public ResponseEntity<List<StableResponseDTO>> getAll() {
        return ResponseEntity.ok(stableService.listStables());
    }

    @GetMapping("/{name}")
    public ResponseEntity<StableResponseDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(stableService.getStable(name));
    }

    @PostMapping
    public ResponseEntity<StableResponseDTO> create(@Valid @RequestBody StableDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stableService.createStable(dto.getName(), dto.getCapacity()));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteStable(@PathVariable String name,
            @RequestParam(value="force", defaultValue = "false")  boolean force) {
        stableService.deleteStable(name,force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{name}/horses")
    public ResponseEntity<List<HorseResponseDTO>> getHorsesSorted(@PathVariable String name) {
        return ResponseEntity.ok(stableService.getHorsesSorted(name));
    }

}
