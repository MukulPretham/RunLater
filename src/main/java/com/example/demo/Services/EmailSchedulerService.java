package com.example.demo.Services;

import com.example.demo.DTOs.EmailJobReq;
import com.example.demo.Entities.EmailJobs;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import com.example.demo.Repository.EmailJobRepository;
import com.example.demo.Repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailSchedulerService {
    JobRepository jobRepository;
    EmailJobRepository emailJobRepository;

    public EmailSchedulerService(EmailJobRepository emailJobRepository, JobRepository jobRepository) {
        this.emailJobRepository = emailJobRepository;
        this.jobRepository = jobRepository;
    }

    public void insertJob(EmailJobReq job){
        Jobs currJob = new Jobs(job.type(), JobStatus.SCHEDULED, job.retryCount());
        jobRepository.save(currJob);
        EmailJobs emailJob = new EmailJobs(currJob,job.from(),job.to(),job.subject(),job.body(),job.contentType());
        emailJobRepository.save(emailJob);
    }

}
