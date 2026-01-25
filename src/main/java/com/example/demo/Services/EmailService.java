package com.example.demo.Services;

import com.example.demo.Entities.EmailJobs;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import com.example.demo.Repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final JobRepository jobRepository;

    public EmailService(JavaMailSender javaMailSender, JobRepository jobRepository) {
        this.javaMailSender = javaMailSender;
        this.jobRepository = jobRepository;
    }

    /**
     * Sends a simple text email.
     */
    public void sendEmail(EmailJobs job) {

        logger.info("Starting email send for jobId={}", job.getId());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(job.getFromEmail());
            message.setTo(job.getToEmail());
            message.setSubject(job.getSubject());
            message.setText(job.getBody());
            javaMailSender.send(message);
            jobRepository.updateJobStatus(job.getJob().getId(), JobStatus.COMPLETED);
            logger.info("Email sent successfully for jobId={}", job.getJob().getId());

        } catch (Exception e) {
            logger.error("Failed to send email for jobId={}", job.getJob().getId(), e);
            jobRepository.updateRetryCounts(job.getJob().getId());
            Jobs updated = jobRepository.findById(job.getJob().getId()).orElse(null);
            if(updated.getRetryCount() == 0){
                jobRepository.updateJobStatus(job.getJob().getId(), JobStatus.FAILED);
            }else {
                jobRepository.updateJobStatus(job.getJob().getId(), JobStatus.RESCHEDULED);
            }
        }
    }
}
