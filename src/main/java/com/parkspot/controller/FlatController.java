package com.parkspot.controller;

import com.parkspot.entity.Flat;
import com.parkspot.service.FlatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing flats (apartments) in the society.
 */
@RestController
@RequestMapping("/api/flats")
@Tag(name = "Flat Management", description = "CRUD operations for society flats")
@CrossOrigin(origins = "*")
public class FlatController {

    private final FlatService flatService;

    public FlatController(FlatService flatService) {
        this.flatService = flatService;
    }

    @PostMapping
    @Operation(summary = "Register a new flat")
    public ResponseEntity<Flat> createFlat(@Valid @RequestBody Flat flat) {
        Flat created = flatService.createFlat(flat);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "List all registered flats")
    public ResponseEntity<List<Flat>> getAllFlats() {
        return ResponseEntity.ok(flatService.getAllFlats());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a flat by ID")
    public ResponseEntity<Flat> getFlatById(@PathVariable Long id) {
        return ResponseEntity.ok(flatService.getFlatById(id));
    }

    @GetMapping("/number/{flatNumber}")
    @Operation(summary = "Get a flat by flat number")
    public ResponseEntity<Flat> getFlatByNumber(@PathVariable String flatNumber) {
        return ResponseEntity.ok(flatService.getFlatByNumber(flatNumber));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a flat's details")
    public ResponseEntity<Flat> updateFlat(@PathVariable Long id, @Valid @RequestBody Flat flat) {
        return ResponseEntity.ok(flatService.updateFlat(id, flat));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a flat")
    public ResponseEntity<Void> deleteFlat(@PathVariable Long id) {
        flatService.deleteFlat(id);
        return ResponseEntity.noContent().build();
    }
}
