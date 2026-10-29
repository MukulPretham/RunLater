package com.example.demo.Repository;

import com.example.demo.Entities.EmailJobs;
import com.example.demo.Entities.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailJobRepository extends JpaRepository<EmailJobs,Long> {
    List<EmailJobs> findByJob(Jobs job);
    Optional<EmailJobs> findByJobId(Long jobId);
}
