package io.github.liias.tms.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TaskModel {
    private Long id; // null if not persisted yet
    private String title;
    private String description;  // nullable

    private LocalDateTime createdAt; // null if not persisted yet
    private LocalDateTime updatedAt; // null if not persisted yet
}