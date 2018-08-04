package io.github.liias.tms.domain.model;

import io.github.liias.tms.domain.data.entity.TaskPriority;
import io.github.liias.tms.domain.data.entity.TaskStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TaskModel {
    private Long id; // null if not persisted yet
    private String title;
    private String description;  // nullable

    // I think it's not a problem to be dependant on data layer for enums, because they should be synchronized
    private TaskPriority priority;
    private TaskStatus status;

    private LocalDateTime dueDate; // nullable
    private LocalDateTime resolvedAt; // nullable

    private LocalDateTime createdAt; // null if not persisted yet
    private LocalDateTime updatedAt; // null if not persisted yet
}