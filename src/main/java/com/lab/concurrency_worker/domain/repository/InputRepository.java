package com.lab.concurrency_worker.domain.repository;

import com.lab.concurrency_worker.domain.model.InputEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InputRepository extends JpaRepository<InputEntity, Long> {

    @Query(value = """
        SELECT * FROM input
        WHERE status = 'pending'
        ORDER BY id
        LIMIT :batchSize
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    List<InputEntity> findBatchForUpdate(@Param("batchSize") int batchSize);
}
