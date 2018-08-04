package io.github.liias.tms.integration;

import io.github.liias.tms.api.controller.TaskController;
import io.github.liias.tms.api.model.Task;
import io.github.liias.tms.api.model.TaskChange;
import io.github.liias.tms.domain.data.entity.TaskEntityPriority;
import io.github.liias.tms.domain.data.entity.TaskEntityStatus;
import io.github.liias.tms.domain.service.TaskScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TaskIntegrationTest {

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskScheduler taskScheduler;

    @Test
    public void fetchAll() {
        TaskChange taskChange = createTaskChange();
        long taskId = taskController.create(taskChange);
        List<Task> fetchAllResultFiltered = taskController.fetchAll().stream()
                .filter(t -> t.getId().equals(taskId))
                .collect(toList());

        assertThat(fetchAllResultFiltered, hasSize(1));
        assertThat(fetchAllResultFiltered, contains(allOf(hasProperty("id", is(taskId)), hasProperty("title", is(taskChange.getTitle())))));
    }

    @Test
    public void create() {
        TaskChange taskChange = createTaskChange();
        long taskId = taskController.create(taskChange);
    }

    @Test
    public void fetch() {
        TaskChange taskChange = createTaskChange();
        long taskId = taskController.create(taskChange);
        Task result = taskController.fetch(taskId);
        assertThat(result.getId(), is(taskId));
        assertThat(result.getTitle(), is(taskChange.getTitle()));
    }

    @Test
    public void update() {
        TaskChange taskChange = createTaskChange();
        long taskId = taskController.create(taskChange);

        taskChange.setTitle("updated");
        taskController.update(taskId, taskChange);

        assertThat(taskController.fetch(taskId).getTitle(), is("updated"));
    }

    @Test
    public void delete() {
        TaskChange taskChange = createTaskChange();
        long taskId = taskController.create(taskChange);

        taskController.delete(taskId);
        assertThat(taskController.fetch(taskId), is(nullValue()));
    }

    @Test
    public void shouldKeepScheduledAndManuallyAddedTasksInOrder() {
        int tasksCountBeforeTest = taskController.fetchAll().size();

        TaskChange taskChange = createTaskChange();
        long manuallyAddedTaskId1 = taskController.create(taskChange);
        long scheduledTaskId1 = taskScheduler.addScheduledTask();
        long manuallyAddedTaskId2 = taskController.create(taskChange);
        long scheduledTaskId2 = taskScheduler.addScheduledTask();

        // make sure updating will not effect ordering
        taskController.update(manuallyAddedTaskId1, taskChange.setTitle("updated"));

        List<Task> tasks = taskController.fetchAll();
        List<Long> returnedTaskIds = tasks.subList(tasksCountBeforeTest, tasks.size()).stream()
                .map(Task::getId)
                .collect(toList());

        assertThat(returnedTaskIds, contains(manuallyAddedTaskId1, scheduledTaskId1, manuallyAddedTaskId2, scheduledTaskId2));
    }

    private static TaskChange createTaskChange() {
        return new TaskChange()
                .setTitle("title")
                .setDescription("description")
                .setPriorityCode(TaskEntityPriority.LOW.name())
                .setStatusCode(TaskEntityStatus.NEW.name());
    }
}
