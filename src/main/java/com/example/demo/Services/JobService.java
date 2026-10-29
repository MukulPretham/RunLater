package com.example.demo.Services;

import com.example.demo.DTOs.*;
import com.example.demo.Entities.*;
import com.example.demo.Exceptions.UserException;
import com.example.demo.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final EmailJobRepository emailJobRepository;
    private final WebhookJobRepository webhookJobRepository;
    private final JobAttemptRepository jobAttemptRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, EmailJobRepository emailJobRepository,
                      WebhookJobRepository webhookJobRepository, JobAttemptRepository jobAttemptRepository,
                      UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.emailJobRepository = emailJobRepository;
        this.webhookJobRepository = webhookJobRepository;
        this.jobAttemptRepository = jobAttemptRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<JobResponse> scheduleEmail(EmailJobReq request, String username, String idempotencyKey) {
        Users user = user(username);
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            List<Jobs> existing = jobRepository.findAllByIdempotencyKeyAndUserUsername(idempotencyKey, username);
            if (!existing.isEmpty()) return existing.stream().map(this::toResponse).toList();
            return createEmailJobs(request, user, idempotencyKey);
        }
        return createEmailJobs(request, user, null);
    }

    private List<JobResponse> createEmailJobs(EmailJobReq request, Users user, String idempotencyKey) {
        return java.util.Arrays.stream(request.to()).map(recipient -> {
            Jobs job = jobRepository.save(new Jobs(user, Job.EMAIL, request.maxAttempts(), request.scheduledAt(), idempotencyKey));
            emailJobRepository.save(new EmailJobs(job, request.from(), recipient, request.subject(), request.body(), request.contentType()));
            return toResponse(job);
        }).toList();
    }

    @Transactional
    public JobResponse scheduleWebhook(WebhookJobReq request, String username, String idempotencyKey) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            return jobRepository.findFirstByIdempotencyKeyAndUserUsername(idempotencyKey, username)
                    .map(this::toResponse).orElseGet(() -> createWebhook(request, user(username), idempotencyKey));
        }
        return createWebhook(request, user(username), null);
    }

    private JobResponse createWebhook(WebhookJobReq request, Users user, String idempotencyKey) {
        Jobs job = jobRepository.save(new Jobs(user, Job.WEBHOOK, request.maxAttempts(), request.scheduledAt(), idempotencyKey));
        webhookJobRepository.save(new WebhookJobs(job, request.targetUrl(), request.payload(), request.secret() == null ? "" : request.secret()));
        return toResponse(job);
    }

    @Transactional
    public JobResponse get(UUID id, String username) { return toResponse(owned(id, username)); }

    @Transactional
    public List<JobResponse> list(String username) {
        return jobRepository.findAll().stream().filter(job -> job.getUser().getUsername().equals(username)).map(this::toResponse).toList();
    }

    @Transactional
    public JobResponse cancel(UUID id, String username) {
        Jobs job = owned(id, username);
        if (!job.cancel()) throw new IllegalStateException("This job can no longer be cancelled");
        return toResponse(job);
    }

    @Transactional
    public JobResponse retry(UUID id, String username) {
        Jobs job = owned(id, username);
        if (!job.retryNow()) throw new IllegalStateException("Only failed or cancelled jobs can be retried");
        return toResponse(job);
    }

    @Transactional
    public JobResponse reschedule(UUID id, Instant scheduledAt, String username) {
        if (scheduledAt == null || scheduledAt.isBefore(Instant.now())) throw new IllegalArgumentException("scheduledAt must be in the future");
        Jobs job = owned(id, username);
        if (!job.reschedule(scheduledAt)) throw new IllegalStateException("This job can no longer be rescheduled");
        return toResponse(job);
    }

    private Users user(String username) {
        return userRepository.findOptionalByUsername(username).orElseThrow(() -> new UserException("User not found"));
    }

    private Jobs owned(UUID id, String username) {
        return jobRepository.findByPublicIdAndUserUsername(id, username).orElseThrow(() -> new UserException("Job not found"));
    }

    private JobResponse toResponse(Jobs job) {
        List<JobAttemptResponse> attempts = jobAttemptRepository.findByJobIdOrderByAttemptedAtDesc(job.getId()).stream()
                .map(a -> new JobAttemptResponse(a.getAttemptNumber(), a.getAttemptedAt(), a.isSuccessful(), a.getDetail())).toList();
        return new JobResponse(job.getPublicId(), job.getType(), job.getStatus(), job.getAttemptCount(), job.getMaxAttempts(),
                job.getScheduledAt(), job.getNextAttemptAt(), job.getCompletedAt(), job.getLastError(), attempts);
    }
}
