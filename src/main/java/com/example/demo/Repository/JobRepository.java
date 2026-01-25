package com.example.demo.Repository;

import com.example.demo.Entities.Job;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface JobRepository extends JpaRepository<Jobs,Long> {
    Jobs findByType(Job job);
    @Query("""
SELECT j FROM Jobs j
WHERE j.status IN :statuses
AND (j.scheduledAt <= :now OR j.scheduledAt IS NULL)
AND j.retryCount > 0
""")
    List<Jobs> findReadyJobs(@Param("now") Instant now,
                             @Param("statuses") List<JobStatus> statuses);

    @Modifying
    @Transactional
    @Query("UPDATE Jobs j SET j.status = :status WHERE j.id = :id")
    int updateJobStatus(@Param("id") Long id,
                        @Param("status") JobStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Jobs j SET j.retryCount = j.retryCount-1 WHERE j.id = :id")
    int updateRetryCounts(@Param("id") Long id);
}
