package com.example.demo.Services;

import com.example.demo.Entities.*;
import com.example.demo.Repository.*;
import com.example.demo.Services.coreServices.EmailService;
import com.example.demo.Services.coreServices.WebhookService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class JobWorker {
    private final JobRepository jobRepository;
    private final EmailJobRepository emailJobRepository;
    private final WebhookJobRepository webhookJobRepository;
    private final JobAttemptRepository jobAttemptRepository;
    private final EmailService emailService;
    private final WebhookService webhookService;

    public JobWorker(JobRepository jobRepository, EmailJobRepository emailJobRepository, WebhookJobRepository webhookJobRepository,
                     JobAttemptRepository jobAttemptRepository, EmailService emailService, WebhookService webhookService) {
        this.jobRepository = jobRepository; this.emailJobRepository = emailJobRepository; this.webhookJobRepository = webhookJobRepository;
        this.jobAttemptRepository = jobAttemptRepository; this.emailService = emailService; this.webhookService = webhookService;
    }

    @Transactional
    public void process(Long jobId) {
        Jobs job = jobRepository.findById(jobId).orElse(null);
        if (job == null || job.getStatus() != JobStatus.PROCESSING) return;
        try {
            if (job.getType() == Job.EMAIL) {
                emailService.sendEmail(emailJobRepository.findByJobId(jobId).orElseThrow());
            } else {
                webhookService.sendWebhook(webhookJobRepository.findByJobId(jobId).orElseThrow());
            }
            job.markCompleted();
            jobAttemptRepository.save(new JobAttempts(job, job.getAttemptCount(), true, "Delivered"));
        } catch (Exception ex) {
            String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
            job.markFailed(message);
            jobAttemptRepository.save(new JobAttempts(job, job.getAttemptCount(), false, message));
        }
    }
}
