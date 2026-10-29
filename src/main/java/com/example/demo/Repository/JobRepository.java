package com.example.demo.Repository;

import com.example.demo.Entities.Job;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Jobs,Long> {
    Optional<Jobs> findByPublicIdAndUserUsername(UUID publicId, String username);
    List<Jobs> findAllByIdempotencyKeyAndUserUsername(String idempotencyKey, String username);
    Optional<Jobs> findFirstByIdempotencyKeyAndUserUsername(String idempotencyKey, String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
SELECT j FROM Jobs j
WHERE j.status IN :statuses
AND j.nextAttemptAt <= :now
ORDER BY j.nextAttemptAt ASC
""")
    List<Jobs> findReadyJobs(@Param("now") Instant now,
                             @Param("statuses") List<JobStatus> statuses);
}
