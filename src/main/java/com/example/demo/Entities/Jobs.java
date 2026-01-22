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

    @Column(nullable = false)
    private int retryCount;

    @Column
    private Instant scheduledAtt;

    public Jobs(Job type, JobStatus status, int retryCount) {
        this.type = type;
        this.status = status;
        this.retryCount = retryCount;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();

        if (this.scheduledAtt == null) {
            this.scheduledAtt = now;
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
        return scheduledAtt;
    }
}
