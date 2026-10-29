package com.example.demo.Entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Job type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private int maxAttempts;

    @Column(nullable = false)
    private int attemptCount;

    @Column
    private Instant scheduledAt;

    @Column(nullable = false)
    private Instant nextAttemptAt;

    private Instant startedAt;

    private Instant completedAt;

    @Column(length = 1000)
    private String lastError;

    @Column
    private String idempotencyKey;

    @Version
    private long version;

    public Jobs(){}

    public Jobs(Users user, Job type, int maxAttempts, Instant scheduledAt, String idempotencyKey) {
        this.user = user;
        this.type = type;
        this.status = JobStatus.SCHEDULED;
        this.maxAttempts = maxAttempts;
        this.scheduledAt = scheduledAt == null ? Instant.now() : scheduledAt;
        this.nextAttemptAt = this.scheduledAt;
        this.idempotencyKey = idempotencyKey;
        this.publicId = UUID.randomUUID();
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (this.publicId == null) this.publicId = UUID.randomUUID();
        if (this.scheduledAt == null) this.scheduledAt = now;
        if (this.nextAttemptAt == null) this.nextAttemptAt = this.scheduledAt;
    }



    public Long getId() {
        return id;
    }

    public Job getType() {
        return type;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public Users getUser() {
        return user;
    }

    public Instant getNextAttemptAt() {
        return nextAttemptAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getLastError() {
        return lastError;
    }

    public void markProcessing() {
        this.status = JobStatus.PROCESSING;
        this.startedAt = Instant.now();
        this.attemptCount++;
    }

    public void markCompleted() {
        this.status = JobStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.lastError = null;
    }

    public void markFailed(String error) {
        this.lastError = error;
        if (attemptCount >= maxAttempts) {
            this.status = JobStatus.FAILED;
            this.completedAt = Instant.now();
        } else {
            this.status = JobStatus.RETRYING;
            long delaySeconds = Math.min(3600, 30L * (1L << Math.min(6, attemptCount - 1)));
            this.nextAttemptAt = Instant.now().plusSeconds(delaySeconds);
        }
    }

    public boolean cancel() {
        if (status == JobStatus.COMPLETED || status == JobStatus.PROCESSING) return false;
        this.status = JobStatus.CANCELLED;
        this.completedAt = Instant.now();
        return true;
    }

    public boolean retryNow() {
        if (status != JobStatus.FAILED && status != JobStatus.CANCELLED) return false;
        this.status = JobStatus.RETRYING;
        this.nextAttemptAt = Instant.now();
        this.completedAt = null;
        this.attemptCount = 0;
        this.lastError = null;
        return true;
    }

    public boolean reschedule(Instant when) {
        if (status == JobStatus.COMPLETED || status == JobStatus.PROCESSING || status == JobStatus.CANCELLED) return false;
        this.status = JobStatus.SCHEDULED;
        this.scheduledAt = when;
        this.nextAttemptAt = when;
        return true;
    }
}
