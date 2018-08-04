package io.github.liias.tms.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Task {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    private String title;
    private String description;

    private TaskPriority priority;
    private TaskStatus status;

    private LocalDateTime dueDate;
    private LocalDateTime resolvedAt;
}
