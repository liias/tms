package io.github.liias.tms.api.model;

import io.github.liias.tms.domain.data.entity.TaskEntityPriority;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class TaskPriority {
    private String code;
    private String label;

    public TaskPriority(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static TaskPriority ofTaskEntityPriority(TaskEntityPriority taskEntityPriority) {
        String label = StringUtils.capitalize(taskEntityPriority.name().toLowerCase());
        return new TaskPriority(taskEntityPriority.name(), label);
    }
}
