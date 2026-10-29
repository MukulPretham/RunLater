package com.example.demo.Repository;

import com.example.demo.Entities.WebhookJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WebhookJobRepository extends JpaRepository<WebhookJobs, Long> {
    Optional<WebhookJobs> findByJobId(Long jobId);
}
