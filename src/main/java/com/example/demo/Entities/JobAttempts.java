package com.example.demo.Entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "job_attempts")
public class JobAttempts {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Jobs job;

    @Column(nullable = false)
    private int attemptNumber;

    @Column(nullable = false)
    private Instant attemptedAt;

    @Column(nullable = false)
    private boolean successful;

    @Column(length = 1000)
    private String detail;

    public JobAttempts() { }
    public JobAttempts(Jobs job, int attemptNumber, boolean successful, String detail) {
        this.job = job;
        this.attemptNumber = attemptNumber;
        this.successful = successful;
        this.detail = detail;
        this.attemptedAt = Instant.now();
    }
    public int getAttemptNumber() { return attemptNumber; }
    public Instant getAttemptedAt() { return attemptedAt; }
    public boolean isSuccessful() { return successful; }
    public String getDetail() { return detail; }
}
