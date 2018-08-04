package io.github.liias.tms.api.model;

import io.github.liias.tms.domain.data.entity.TaskEntityStatus;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class TaskStatus {
    private String code;
    private String label;

    public TaskStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static TaskStatus ofTaskEntityStatus(TaskEntityStatus taskEntityStatus) {
        String label = StringUtils.capitalize(taskEntityStatus.name().toLowerCase());
        return new TaskStatus(taskEntityStatus.name(), label);
    }
}
