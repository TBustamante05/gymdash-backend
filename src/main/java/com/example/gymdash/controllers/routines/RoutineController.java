package com.example.gymdash.controllers.routines;

import com.example.gymdash.dtos.routines.*;
import com.example.gymdash.services.routines.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    // Rutinas

    @GetMapping
    public ResponseEntity<List<RoutineResponse>> getAll() {
        return ResponseEntity.ok(routineService.getAllRoutines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(routineService.getRoutineById(id));
    }

    @PostMapping
    public ResponseEntity<RoutineResponse> create(@RequestBody RoutineRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineService.createRoutine(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineResponse> update(
        @PathVariable Long id, @RequestBody RoutineRequest req) {
        return ResponseEntity.ok(routineService.updateRoutine(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build(); // 204 No content
    }

    // Ejercicios

    @GetMapping("/{routineId}/exercises")
    public ResponseEntity<List<ExerciseResponse>> getExercises(
        @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.getExercises(routineId));
    }

    @PostMapping("/{routineId}/exercises")
    public ResponseEntity<ExerciseResponse> addExercise(
        @PathVariable Long routineId,
        @RequestBody ExerciseRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineService.addExercise(routineId, req));
    }

    @PutMapping("/{routineId}/exercises/{exerciseId}")
    public ResponseEntity<ExerciseResponse> updateExercise(
        @PathVariable Long routineId,
        @PathVariable Long exerciseId,
        @RequestBody ExerciseRequest req
    ) {
        return ResponseEntity.ok(routineService.updateExercise(routineId, exerciseId, req));
    }

    @DeleteMapping("/{routineId}/exercises/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
        @PathVariable Long routineId,
        @PathVariable Long exerciseId
    ) {
        routineService.deleteExercise(routineId, exerciseId);
        return ResponseEntity.noContent().build();
    }

    // ---------------------- REODER ------------------------
    @PutMapping("/{routineId}/exercises/reorder")
    public ResponseEntity<Void> reorderExercises(
            @PathVariable Long routineId,
            @RequestBody ReorderRequest req
    ) {
        routineService.reorderExercises(routineId, req);
        return ResponseEntity.noContent().build();
    }

}
