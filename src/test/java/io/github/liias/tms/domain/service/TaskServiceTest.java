package io.github.liias.tms.domain.service;

import io.github.liias.tms.domain.data.entity.TaskEntity;
import io.github.liias.tms.domain.data.entity.TaskEntityPriority;
import io.github.liias.tms.domain.data.entity.TaskEntityStatus;
import io.github.liias.tms.domain.data.repository.TaskRepository;
import io.github.liias.tms.domain.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskServiceTest {
    private TaskRepository mockTaskRepository;
    private TaskService taskService;

    @Before
    public void before() {
        // setting mockTaskRepository value here, so it resets assigned when() expectations between tests
        mockTaskRepository = mock(TaskRepository.class);
        taskService = new TaskService(mockTaskRepository);
    }

    @Test
    public void fetchAll() {
        when(mockTaskRepository.findAllByOrderByCreatedAtAsc()).thenReturn(new ArrayList<>());
        assertThat(taskService.fetchAll(), is(empty()));

        TaskEntity taskEntity = createTaskEntity();
        when(mockTaskRepository.findAllByOrderByCreatedAtAsc()).thenReturn(Stream.of(taskEntity).collect(toList()));
        assertThat(taskService.fetchAll(), contains(createTaskModel(taskEntity)));
    }

    @Test
    public void create() {
        TaskEntity taskEntity = createTaskEntity();
        when(mockTaskRepository.save(any())).thenReturn(taskEntity);
        assertThat(taskService.create(createTaskModel()), is(taskEntity.getId()));
    }

    @Test
    public void fetch() {
        when(mockTaskRepository.findById(any())).thenReturn(Optional.empty());
        assertThat(taskService.fetch(1L), is(Optional.empty()));

        TaskEntity taskEntity = createTaskEntity();
        when(mockTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        assertThat(taskService.fetch(1L), is(Optional.of(createTaskModel(taskEntity))));
    }

    @Test
    public void update() {
        TaskModel taskModel = createTaskModel();

        TaskEntity oldTaskEntity = new TaskEntity();
        oldTaskEntity.setId(taskModel.getId());
        when(mockTaskRepository.findById(any())).thenReturn(Optional.of(oldTaskEntity));

        taskService.update(taskModel);
        ArgumentCaptor<TaskEntity> argument = ArgumentCaptor.forClass(TaskEntity.class);
        Mockito.verify(mockTaskRepository).save(argument.capture());
        TaskEntity newTaskEntity = argument.getValue();
        assertThat(newTaskEntity.getId(), is(taskModel.getId()));
        assertThat(newTaskEntity.getTitle(), is(taskModel.getTitle()));
    }

    @Test
    public void delete() {
        long taskId = 1L;
        taskService.delete(taskId);

        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTaskRepository).deleteById(argument.capture());
        assertThat(argument.getValue(), is(taskId));
    }

    private static TaskEntity createTaskEntity() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTitle("title");
        taskEntity.setDescription("description");
        taskEntity.setPriority(TaskEntityPriority.UNKNOWN);
        taskEntity.setStatus(TaskEntityStatus.NEW);
        return taskEntity;
    }

    private static TaskModel createTaskModel() {
        return createTaskModel(createTaskEntity());
    }

    private static TaskModel createTaskModel(TaskEntity taskEntity) {
        return new TaskModel()
                .setId(taskEntity.getId())
                .setTitle(taskEntity.getTitle())
                .setDescription(taskEntity.getDescription())
                .setPriority(taskEntity.getPriority())
                .setStatus(taskEntity.getStatus());
    }
}
