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

import static io.github.liias.tms.util.DateUtil.toLocalDateTime;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService {
    private final TaskRepository taskRepository;

    // fetch all tasks (ordered)
    @Transactional
    public List<TaskModel> fetchAll() {
        return taskRepository.findAll().stream()
                .map(TaskService::toModel)
                .collect(toList());
    }

    @Transactional
    public long create(TaskModel taskModel) {
        TaskEntity taskEntity = new TaskEntity();
        updateEntityFields(taskEntity, taskModel);
        taskEntity = taskRepository.save(taskEntity);
        return taskEntity.getId();
    }

    @Transactional
    public Optional<TaskModel> fetch(long taskId) {
        return taskRepository.findById(taskId)
                .map(TaskService::toModel);
    }

    @Transactional
    public void update(TaskModel taskModel) {
        TaskEntity taskEntity = taskRepository.findById(taskModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("No task with id " + taskModel.getId() + " found"));

        updateEntityFields(taskEntity, taskModel);
        taskRepository.save(taskEntity);
    }

    @Transactional
    public void delete(long taskId) {
        taskRepository.deleteById(taskId);
    }

    private static void updateEntityFields(TaskEntity taskEntity, TaskModel taskModel) {
        taskEntity.setTitle(taskModel.getTitle());
        taskEntity.setDescription(taskModel.getDescription());
    }

    private static TaskModel toModel(TaskEntity taskEntity) {
        return new TaskModel()
                .setId(taskEntity.getId())
                .setTitle(taskEntity.getTitle())
                .setDescription(taskEntity.getDescription())
                .setCreatedAt(toLocalDateTime(taskEntity.getCreatedAt()))
                .setUpdatedAt(toLocalDateTime(taskEntity.getUpdatedAt()));
    }
}
