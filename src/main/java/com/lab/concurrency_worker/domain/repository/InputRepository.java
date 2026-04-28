package com.lab.concurrency_worker.domain.repository;

import com.lab.concurrency_worker.domain.model.InputEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InputRepository extends JpaRepository<InputEntity, Long> {

    @Query(value = """
        SELECT * FROM input
        WHERE status = 'pending'
        ORDER BY id
        LIMIT 1
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    Optional<InputEntity> findNextPendingForUpdate();
}
