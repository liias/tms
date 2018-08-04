package io.github.liias.tms.domain.service;

import io.github.liias.tms.domain.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

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
        taskScheduler.addScheduledTask();
        ArgumentCaptor<TaskModel> argument = ArgumentCaptor.forClass(TaskModel.class);
        Mockito.verify(mockTaskService).create(argument.capture());
    }
}
