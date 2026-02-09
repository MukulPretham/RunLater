package com.example.demo.Services.coreServices;

import com.example.demo.Entities.Job;
import com.example.demo.Repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebHookService {
    private static final Logger logger =
            LoggerFactory.getLogger(WebHookService.class);

    private final RestTemplate restTemplate;
    private final JobRepository jobRepository;

    public WebHookService(RestTemplate restTemplate,
                          JobRepository jobRepository) {
        this.restTemplate = restTemplate;
        this.jobRepository = jobRepository;
    }

    public void sendWebhook(Job job) {
    }

}
