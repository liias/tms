package io.github.liias.tms.domain.service;

import io.github.liias.tms.domain.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskSchedulerTest {
    private TaskService mockTaskService;
    private TaskScheduler taskScheduler;

    @Before
    public void before() {
        // setting mockTaskRepository value here, so it resets assigned when() expectations between tests
        mockTaskService = mock(TaskService.class);
        taskScheduler = new TaskScheduler(mockTaskService);
    }

    @Test
    public void addScheduledTask() {
        when(mockTaskService.create(any())).thenReturn(new TaskModel());
        taskScheduler.addScheduledTask();
        ArgumentCaptor<TaskModel> argument = ArgumentCaptor.forClass(TaskModel.class);
        Mockito.verify(mockTaskService).create(argument.capture());
    }
}
