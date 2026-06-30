package app.controller;

import app.dto.HorseResponseDTO;
import app.dto.StableDTO;
import app.dto.StableResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.service.StableService;

import java.util.List;

@RestController
@RequestMapping("/api/stables")
@Tag(name = "Stable Management", description = "Endpoints for managing stables")
public class StableController {
    private final StableService stableService;

    public StableController(StableService stableService) {
        this.stableService = stableService;
    }

    @Operation(summary = "Get all stables", description = "Retrieve a list of all stables")
    @GetMapping
    public ResponseEntity<List<StableResponseDTO>> getAll() {
        return ResponseEntity.ok(stableService.listStables());
    }

    @Operation(summary = "Get stable by name", description = "Retrieve a stable by its unique name")
    @GetMapping("/{name}")
    public ResponseEntity<StableResponseDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(stableService.getStable(name));
    }

    @Operation(summary = "Add a stable", description = "Add a new stable to database")
    @PostMapping
    public ResponseEntity<StableResponseDTO> create(@Valid @RequestBody StableDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stableService.createStable(dto.getName(), dto.getCapacity()));
    }

    @Operation(summary = "Delete a stable", description = "Delete a stable by its unique name")
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteStable(@PathVariable String name,
            @RequestParam(value="force", defaultValue = "false")  boolean force) {
        stableService.deleteStable(name,force);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get horses in a stable", description = "Retrieve a list of horses in a specific " +
            "stable by it's unique name")
    @GetMapping("/{name}/horses")
    public ResponseEntity<List<HorseResponseDTO>> getHorsesSorted(@PathVariable String name) {
        return ResponseEntity.ok(stableService.getHorsesSorted(name));
    }

    @Operation(summary = "Update stable", description = "Update an existing stable's details")
    @PutMapping("/{name}")
    public ResponseEntity<StableResponseDTO> updateStable(@PathVariable String name,
            @Valid @RequestBody StableDTO dto) {
        return ResponseEntity.ok(stableService.updateStable(name, dto));
    }
}
