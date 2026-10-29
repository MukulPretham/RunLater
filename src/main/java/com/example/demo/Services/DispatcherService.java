package com.example.demo.Services;

import com.example.demo.Entities.JobStatus;
import com.example.demo.Entities.Jobs;
import com.example.demo.Repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class DispatcherService {
    private JobRepository jobRepository;
    private Executor taskExecutor;
    private JobWorker jobWorker;

    public DispatcherService(Executor taskExecutor, JobWorker jobWorker, JobRepository jobRepository) {
        this.taskExecutor = taskExecutor;
        this.jobWorker = jobWorker;
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedDelayString = "${app.dispatcher.poll-interval-ms:5000}")
    @Transactional
    public void dispatch(){
        List<JobStatus> allowed = List.of(JobStatus.SCHEDULED, JobStatus.RETRYING);
        List<Jobs>jobs =  jobRepository.findReadyJobs(Instant.now(),allowed);
        for(Jobs job : jobs){
            job.markProcessing();
            Long jobId = job.getId();
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    taskExecutor.execute(() -> jobWorker.process(jobId));
                }
            });
        }
    }
}
