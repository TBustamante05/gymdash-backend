package com.example.gymdash.services.routines;

import com.example.gymdash.dtos.routines.*;
import com.example.gymdash.entities.Exercise;
import com.example.gymdash.entities.User;
import com.example.gymdash.entities.WorkoutRoutine;
import com.example.gymdash.repositories.ExerciseRepository;
import com.example.gymdash.repositories.WorkoutRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final WorkoutRoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    // Obtener el usuario del token JWT
    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No hay sesión activa");
        }

        return (User) authentication.getPrincipal();
    }

    // Rutinas
    public List<RoutineResponse> getAllRoutines() {
        Long userId = getCurrentUser().getId();
        return routineRepository.findAllByUserId(userId)
                .stream()
                .map(this::toRoutineResponse)
                .toList();
    }

    public RoutineResponse getRoutineById(Long id) {
        Long userId = getCurrentUser().getId();
        return toRoutineResponse(
            routineRepository.findByIdAndUserIdOrThrow(id, userId)
        );
    }

    public RoutineResponse createRoutine(RoutineRequest req) {
        User user = getCurrentUser();
        WorkoutRoutine routine = WorkoutRoutine.builder()
                .name(req.name())
                .description(req.description())
                .user(user)
                .build();
        return toRoutineResponse(routineRepository.save(routine));
    }

    public RoutineResponse updateRoutine(Long id, RoutineRequest req) {
        Long userId = getCurrentUser().getId();
        WorkoutRoutine routine = routineRepository
                .findByIdAndUserIdOrThrow(id, userId);

        routine.setName(req.name());
        routine.setDescription(req.description());
        return toRoutineResponse(routineRepository.save(routine));
    }

    public void deleteRoutine(Long id) {
        Long userId = getCurrentUser().getId();
        routineRepository.delete(
            routineRepository.findByIdAndUserIdOrThrow(id, userId)
        );
    }

    // Ejercicios
    public List<ExerciseResponse> getExercises(Long routineId) {
        Long userId = getCurrentUser().getId();
        // Verificar que la rutina pertenece al usuario
        routineRepository.findByIdAndUserIdOrThrow(routineId, userId);

        return exerciseRepository.findAllByRoutineId(routineId)
                .stream()
                .sorted(Comparator.comparingInt(Exercise::getPosition))
                .map(this::toExerciseResponse)
                .toList();
    }

    public ExerciseResponse addExercise(Long routineId, ExerciseRequest req) {
        Long userId = getCurrentUser().getId();
        WorkoutRoutine routine = routineRepository
                .findByIdAndUserIdOrThrow(routineId, userId);

        int nextPosition = exerciseRepository.findAllByRoutineId(routineId).size();

        Exercise exercise = Exercise.builder()
                .name(req.name())
                .sets(req.sets())
                .reps(req.reps())
                .restTime(req.restTime())
                .weight(req.weight())
                .position(nextPosition)
                .routine(routine)
                .build();

        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    public ExerciseResponse updateExercise(Long routineId, Long exerciseId, ExerciseRequest req) {
        Long userId = getCurrentUser().getId();
        routineRepository.findByIdAndUserIdOrThrow(routineId, userId);

        Exercise exercise = exerciseRepository
                .findByIdAndRoutineIdOrThrow(exerciseId, routineId);

        exercise.setName(req.name());
        exercise.setSets(req.sets());
        exercise.setReps(req.reps());
        exercise.setWeight(req.weight());
        exercise.setRestTime(req.restTime());

        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    public void reorderExercises(Long routineId, ReorderRequest req) {
        Long userId = getCurrentUser().getId();
        routineRepository.findByIdAndUserIdOrThrow(routineId, userId);

        List<Long> ids = req.exerciseIds();
        for (int i = 0; i < ids.size(); i++) {
            Exercise exercise = exerciseRepository
                    .findByIdAndRoutineIdOrThrow(ids.get(i), routineId);
            exercise.setPosition(i);
            exerciseRepository.save(exercise);
        }
    }

    public void deleteExercise(Long routineId, Long exerciseId) {
        Long userId = getCurrentUser().getId();
        routineRepository.findByIdAndUserIdOrThrow(routineId, userId);

        Exercise exercise = exerciseRepository
                .findByIdAndRoutineIdOrThrow(exerciseId, routineId);

        exerciseRepository.delete(exercise);
    }
    // Mappers
    private RoutineResponse toRoutineResponse(WorkoutRoutine r) {
        List<ExerciseResponse> exercises = r.getExercises()
                .stream()
                .sorted(Comparator.comparingInt(Exercise::getPosition))
                .map(this::toExerciseResponse)
                .toList();

        return new RoutineResponse(
                r.getId(), r.getName(), r.getDescription(), r.getCreatedAt(), exercises
        );
    }

    private ExerciseResponse toExerciseResponse(Exercise e) {
        return new ExerciseResponse(
                e.getId(), e.getName(), e.getSets(), e.getReps(), e.getWeight(), e.getRestTime(), e.getPosition()
        );
    }
}
