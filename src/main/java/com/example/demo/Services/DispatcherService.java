package com.example.demo.Services;

import com.example.demo.Entities.EmailJobs;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import com.example.demo.Repository.EmailJobRepository;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Services.coreServices.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class DispatcherService {
    private JobRepository jobRepository;
    private EmailJobRepository emailJobRepository;
    private EmailService emailService;
    private Executor taskExecutor;

    public DispatcherService(Executor taskExecutor, EmailService emailService, EmailJobRepository emailJobRepository, JobRepository jobRepository) {
        this.taskExecutor = taskExecutor;
        this.emailService = emailService;
        this.emailJobRepository = emailJobRepository;
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void dispatch(){
        List<JobStatus> allowed = new ArrayList<>();
        allowed.add(JobStatus.SCHEDULED);
        allowed.add(JobStatus.RESCHEDULED);
        List<Jobs>jobs =  jobRepository.findReadyJobs(Instant.now(),allowed);
        System.out.println(jobs);
        for(Jobs job : jobs){
            switch (job.getType()){
                case EMAIL :
                    EmailJobs emailJobs = emailJobRepository.findByJob(job).get(0);

                    if(emailJobs == null){
                        continue;
                    }
                    System.out.println(emailJobs.getToEmail());
                    taskExecutor.execute(()-> emailService.sendEmail(emailJobs));
                case WEBHOOK:

            }
        }
    }
}
