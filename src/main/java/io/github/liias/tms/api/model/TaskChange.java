package io.github.liias.tms.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// properties needed only for creating and updating
@Data
@Accessors(chain = true)
public class TaskChange {
    private String title;
    private String description;

    private String priorityCode;
    private String statusCode;

    private LocalDateTime dueDate;
    private LocalDateTime resolvedAt;
}
