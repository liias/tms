package io.github.liias.tms.domain.data.repository;

import io.github.liias.tms.domain.data.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    List<TaskEntity> findAllByOrderByCreatedAtAsc();
}
