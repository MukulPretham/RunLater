package com.example.demo.Controllers;

import com.example.demo.DTOs.EmailJobReq;
import com.example.demo.Entities.JobStatus;
import com.example.demo.Services.EmailSchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("schedule")
public class SchedulerController {
    private EmailSchedulerService emailSchedulerService;

    public SchedulerController(EmailSchedulerService emailSchedulerService) {
        this.emailSchedulerService = emailSchedulerService;
    }

    @PostMapping("/email")
    public ResponseEntity<JobStatus> schedule(@RequestBody EmailJobReq req){
        emailSchedulerService.insertJob(req);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(JobStatus.SCHEDULED);
    }

}
