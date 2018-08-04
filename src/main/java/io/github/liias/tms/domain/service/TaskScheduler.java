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
        addScheduledTaskInner();
    }

    // extracted for testing purposes (and because scheduler method should return void)
    public TaskModel addScheduledTaskInner() {
        TaskModel taskModel = createScheduledTask();
        taskModel = taskService.create(taskModel);
        log.info("Added scheduled task {}", taskModel.getId());
        return taskModel;
    }

    private static TaskModel createScheduledTask() {
        return new TaskModel()
                .setTitle("Scheduled task")
                .setDescription("Scheduled task description")
                .setPriority(TaskEntityPriority.UNKNOWN)
                .setStatus(TaskEntityStatus.NEW);
    }
}
