package io.github.liias.tms.domain.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "TASK")
public class TaskEntity extends AuditedEntity {
    private static final String SQ_TASK_ID = "SQ_TASK_ID";

    @Id
    @GeneratedValue(generator = SQ_TASK_ID)
    @SequenceGenerator(name = SQ_TASK_ID, sequenceName = SQ_TASK_ID)
    private Long id;

    @Column(name = "TITLE", length = 200, nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
}
