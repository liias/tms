package io.github.liias.tms.api.controller;


import io.github.liias.tms.api.model.Task;
import io.github.liias.tms.api.model.TaskChange;
import io.github.liias.tms.api.model.TaskPriority;
import io.github.liias.tms.api.model.TaskStatus;
import io.github.liias.tms.domain.data.entity.TaskEntityPriority;
import io.github.liias.tms.domain.data.entity.TaskEntityStatus;
import io.github.liias.tms.domain.model.TaskModel;
import io.github.liias.tms.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {
    private final TaskService taskService;

    @RequestMapping(method = GET)
    public List<Task> fetchAll() {
        return taskService.fetchAll().stream()
                .map(TaskController::toTask)
                .collect(toList());
    }

    @RequestMapping(method = POST)
    public TaskModel create(@RequestBody TaskChange taskChange) {
        return taskService.create(toTaskModel(null, taskChange));
    }

    @RequestMapping("/{taskId}")
    public Task fetch(@PathVariable("taskId") long taskId) {
        return taskService.fetch(taskId)
                .map(TaskController::toTask)
                .orElse(null);
    }

    @RequestMapping(value = "/{taskId}", method = POST)
    public TaskModel update(@PathVariable("taskId") long taskId, @RequestBody TaskChange taskChange) {
        return taskService.update(toTaskModel(taskId, taskChange));
    }

    @RequestMapping(value = "/{taskId}", method = DELETE)
    public void delete(@PathVariable("taskId") long taskId) {
        taskService.delete(taskId);
    }

    private static TaskModel toTaskModel(Long taskId, TaskChange taskChange) {
        return new TaskModel()
                .setId(taskId)
                .setTitle(taskChange.getTitle())
                .setDescription(taskChange.getDescription())
                .setPriority(TaskEntityPriority.valueOf(taskChange.getPriorityCode()))
                .setStatus(TaskEntityStatus.valueOf(taskChange.getStatusCode()))
                .setDueDate(taskChange.getDueDate())
                .setResolvedAt(taskChange.getResolvedAt());
    }

    private static Task toTask(TaskModel taskModel) {
        Task task = new Task();
        task.setId(taskModel.getId());
        task.setTitle(taskModel.getTitle());
        task.setDescription(taskModel.getDescription());
        task.setPriority(TaskPriority.ofTaskEntityPriority(taskModel.getPriority()));
        task.setStatus(TaskStatus.ofTaskEntityStatus(taskModel.getStatus()));
        task.setDueDate(taskModel.getDueDate());
        task.setResolvedAt(taskModel.getResolvedAt());
        task.setCreatedAt(taskModel.getCreatedAt());
        task.setUpdateAt(taskModel.getUpdatedAt());
        return task;
    }
}
