package com.example.gymdash.dtos.routines;

import java.util.List;

public record ReorderRequest(
    List<Long> exerciseIds
) {
}
