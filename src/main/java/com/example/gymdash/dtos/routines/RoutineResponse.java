package com.example.gymdash.dtos.routines;

import java.util.Date;
import java.util.List;

public record RoutineResponse(
    Long id,
    String name,
    String description,
    Date createdAt,
    List<ExerciseResponse> exercises
) {}
