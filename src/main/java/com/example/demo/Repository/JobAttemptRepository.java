package com.example.demo.Repository;

import com.example.demo.Entities.JobAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobAttemptRepository extends JpaRepository<JobAttempts, Long> {
    List<JobAttempts> findByJobIdOrderByAttemptedAtDesc(Long jobId);
}
