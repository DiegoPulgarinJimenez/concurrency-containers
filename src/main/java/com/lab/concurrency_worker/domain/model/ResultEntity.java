package com.lab.concurrency_worker.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "result")
@Getter
@Setter
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "input_id")
    private Long inputId;

    private String workerIdentifier;

    private String result;

    private LocalDateTime date;
}
