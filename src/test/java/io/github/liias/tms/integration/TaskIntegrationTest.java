package io.github.liias.tms.integration;

import io.github.liias.tms.api.controller.TaskController;
import io.github.liias.tms.api.model.Task;
import io.github.liias.tms.api.model.TaskChange;
import io.github.liias.tms.domain.data.entity.TaskEntityPriority;
import io.github.liias.tms.domain.data.entity.TaskEntityStatus;
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

    private static TaskChange createTaskChange() {
        return new TaskChange()
                .setTitle("title")
                .setDescription("description")
                .setPriorityCode(TaskEntityPriority.LOW.name())
                .setStatusCode(TaskEntityStatus.NEW.name());
    }
}
