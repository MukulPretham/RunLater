package com.example.demo.DTOs;

import com.example.demo.Entities.Job;
import com.example.demo.Entities.JobStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record JobResponse(
        UUID id, Job channel, JobStatus status, int attemptCount, int maxAttempts,
        Instant scheduledAt, Instant nextAttemptAt, Instant completedAt, String lastError,
        List<JobAttemptResponse> attempts
) { }
