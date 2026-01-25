package com.example.demo.Entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "jobs")
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Job type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = true)
    private int retryCount;

    @Column
    private Instant scheduledAt;

    public Jobs(){}

    public Jobs(Job type, JobStatus status, int retryCount) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.retryCount = retryCount;
    }

    public Jobs(Job type, JobStatus status, int retryCount, Instant scheduledAtt) {
        this.type = type;
        this.status = status;
        this.retryCount = retryCount;
        this.scheduledAt = scheduledAtt;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();

        if (this.scheduledAt == null) {
            this.scheduledAt = now;
        }
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

    public Instant getScheduledAtt() {
        return scheduledAt;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
