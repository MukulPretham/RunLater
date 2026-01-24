package com.example.demo.Services;

import com.example.demo.DTOs.EmailJobReq;
import com.example.demo.Entities.EmailJobs;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import com.example.demo.Repository.EmailJobRepository;
import com.example.demo.Repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailSchedulerService {
    JobRepository jobRepository;
    EmailJobRepository emailJobRepository;

    public EmailSchedulerService(EmailJobRepository emailJobRepository, JobRepository jobRepository) {
        this.emailJobRepository = emailJobRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional
    public void insertJob(EmailJobReq job){
        for(String email : job.to()){
            Jobs currJob = new Jobs(job.type(), JobStatus.SCHEDULED, job.retryCount(),job.scheduledAt());
            jobRepository.save(currJob);
            EmailJobs emailJob = new EmailJobs(currJob,job.from(),email,job.subject(),job.body(),job.contentType());
            emailJobRepository.save(emailJob);
        }
    }

}
