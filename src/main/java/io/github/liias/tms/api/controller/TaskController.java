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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(TaskController.TASKS_API_PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {
    static final String TASKS_API_PATH = "/api/v1/tasks";
    private static final String FETCH_PATH = "/{taskId}";
    private final TaskService taskService;

    @RequestMapping(method = GET)
    public List<Task> fetchAll() {
        return taskService.fetchAll().stream()
                .map(TaskController::toTask)
                .collect(toList());
    }

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskModel create(@RequestBody TaskChange taskChange, HttpServletResponse response) {
        TaskModel taskModel = taskService.create(toTaskModel(null, taskChange));
        response.setHeader(HttpHeaders.LOCATION, createPath(FETCH_PATH, taskModel.getId()));
        return taskModel;
    }

    private static String createPath(String path, Object... params) {
        return UriComponentsBuilder
                .fromUriString(TASKS_API_PATH + path)
                .buildAndExpand(params)
                .toUriString();
    }

    @RequestMapping(FETCH_PATH)
    public Task fetch(@PathVariable("taskId") long taskId) {
        return taskService.fetch(taskId)
                .map(TaskController::toTask)
                .orElse(null);
    }

    @RequestMapping(value = "/{taskId}", method = PUT)
    public TaskModel update(@PathVariable("taskId") long taskId, @RequestBody TaskChange taskChange) {
        return taskService.update(toTaskModel(taskId, taskChange));
    }

    @RequestMapping(value = "/{taskId}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
