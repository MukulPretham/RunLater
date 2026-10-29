package com.example.demo.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "webhook_jobs")
public class WebhookJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, unique = true)
    private Jobs job;

    @Column(nullable = false, length = 2048)
    private String targetUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String secret;

    public WebhookJobs() { }

    public WebhookJobs(Jobs job, String targetUrl, String payload, String secret) {
        this.job = job;
        this.targetUrl = targetUrl;
        this.payload = payload;
        this.secret = secret;
    }

    public Jobs getJob() { return job; }
    public String getTargetUrl() { return targetUrl; }
    public String getPayload() { return payload; }
    public String getSecret() { return secret; }
}
