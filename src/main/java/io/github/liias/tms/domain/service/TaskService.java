package io.github.liias.tms.domain.service;

import io.github.liias.tms.domain.data.entity.TaskEntity;
import io.github.liias.tms.domain.data.repository.TaskRepository;
import io.github.liias.tms.domain.model.TaskModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.liias.tms.util.DateUtil.toDate;
import static io.github.liias.tms.util.DateUtil.toLocalDateTime;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService {
    private final TaskRepository taskRepository;

    // fetch all tasks (ordered by creation time)
    @Transactional
    public List<TaskModel> fetchAll() {
        return taskRepository.findAllByOrderByCreatedAtAsc().stream()
                .map(TaskService::toModel)
                .collect(toList());
    }

    @Transactional
    public TaskModel create(TaskModel taskModel) {
        TaskEntity taskEntity = new TaskEntity();
        updateEntityFields(taskEntity, taskModel);
        taskEntity = taskRepository.save(taskEntity);
        return toModel(taskEntity);
    }

    @Transactional
    public Optional<TaskModel> fetch(long taskId) {
        return taskRepository.findById(taskId)
                .map(TaskService::toModel);
    }

    @Transactional
    public TaskModel update(TaskModel taskModel) {
        TaskEntity taskEntity = taskRepository.findById(taskModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("No task with id " + taskModel.getId() + " found"));

        updateEntityFields(taskEntity, taskModel);
        taskEntity = taskRepository.save(taskEntity);
        return toModel(taskEntity);
    }

    @Transactional
    public void delete(long taskId) {
        taskRepository.deleteById(taskId);
    }

    private static void updateEntityFields(TaskEntity taskEntity, TaskModel taskModel) {
        taskEntity.setTitle(taskModel.getTitle());
        taskEntity.setDescription(taskModel.getDescription());
        taskEntity.setPriority(taskModel.getPriority());
        taskEntity.setStatus(taskModel.getStatus());
        taskEntity.setDueDate(toDate(taskModel.getDueDate()));
        taskEntity.setResolvedAt(toDate(taskModel.getResolvedAt()));
    }

    private static TaskModel toModel(TaskEntity taskEntity) {
        return new TaskModel()
                .setId(taskEntity.getId())
                .setTitle(taskEntity.getTitle())
                .setDescription(taskEntity.getDescription())
                .setPriority(taskEntity.getPriority())
                .setStatus(taskEntity.getStatus())
                .setDueDate(toLocalDateTime(taskEntity.getDueDate()))
                .setResolvedAt(toLocalDateTime(taskEntity.getResolvedAt()))
                .setCreatedAt(toLocalDateTime(taskEntity.getCreatedAt()))
                .setUpdatedAt(toLocalDateTime(taskEntity.getUpdatedAt()));
    }
}
