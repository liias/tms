package io.github.liias.tms.domain.service;

import io.github.liias.tms.domain.data.entity.TaskEntityPriority;
import io.github.liias.tms.domain.data.entity.TaskEntityStatus;
import io.github.liias.tms.domain.model.TaskModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskScheduler {
    private static final int SCHEDULED_TASK_INTERVAL_MS = 5000;

    private final TaskService taskService;

    @Scheduled(fixedRate = SCHEDULED_TASK_INTERVAL_MS)
    public void addScheduledTask() {
        TaskModel taskModel = createScheduledTask();
        long taskId = taskService.create(taskModel);
        log.info("Added scheduled task {}", taskId);
    }

    private static TaskModel createScheduledTask() {
        return new TaskModel()
                .setTitle("Scheduled task")
                .setDescription("Scheduled task description")
                .setPriority(TaskEntityPriority.GENERATED)
                .setStatus(TaskEntityStatus.NEW);
    }
}
