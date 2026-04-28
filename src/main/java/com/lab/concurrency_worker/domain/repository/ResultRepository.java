package com.lab.concurrency_worker.domain.repository;

import com.lab.concurrency_worker.domain.model.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<ResultEntity, Long> {
}
