package com.example.demo.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "email_jobs")
public class EmailJobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Jobs job;

    @Column(nullable = false)
    private String fromEmail;

    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String contentType;

    public EmailJobs() {}

    public EmailJobs(Jobs job, String fromEmail, String toEmail,
                     String subject, String body, String contentType) {
        this.job = job;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    public Jobs getJob() {
        return job;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
