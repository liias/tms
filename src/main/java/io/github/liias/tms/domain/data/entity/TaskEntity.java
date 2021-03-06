package io.github.liias.tms.domain.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Setter
@Getter
@Entity
@Table(name = "TASK")
public class TaskEntity extends AuditedEntity {
    private static final String SQ_TASK_ID_GENERATOR = "SQ_TASK_ID_GENERATOR";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SQ_TASK_ID_GENERATOR)
    @SequenceGenerator(name = SQ_TASK_ID_GENERATOR, sequenceName = "SQ_TASK_ID")
    private Long id;

    @Column(name = "TITLE", length = 200, nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Column(name = "PRIORITY", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskEntityPriority priority;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskEntityStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESOLVED_AT")
    private Date resolvedAt;
}
