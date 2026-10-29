package com.example.demo.Controllers;

import com.example.demo.DTOs.*;
import com.example.demo.Services.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class SchedulerController {
    private final JobService jobService;

    public SchedulerController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/email")
    public ResponseEntity<List<JobResponse>> scheduleEmail(@Valid @RequestBody EmailJobReq req,
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey, Authentication auth) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jobService.scheduleEmail(req, auth.getName(), idempotencyKey));
    }

    @PostMapping("/webhook")
    public ResponseEntity<JobResponse> scheduleWebhook(@Valid @RequestBody WebhookJobReq req,
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey, Authentication auth) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jobService.scheduleWebhook(req, auth.getName(), idempotencyKey));
    }

    @GetMapping
    public List<JobResponse> list(Authentication auth) { return jobService.list(auth.getName()); }

    @GetMapping("/{id}")
    public JobResponse get(@PathVariable UUID id, Authentication auth) { return jobService.get(id, auth.getName()); }

    @PostMapping("/{id}/cancel")
    public JobResponse cancel(@PathVariable UUID id, Authentication auth) { return jobService.cancel(id, auth.getName()); }

    @PostMapping("/{id}/retry")
    public JobResponse retry(@PathVariable UUID id, Authentication auth) { return jobService.retry(id, auth.getName()); }

    @PatchMapping("/{id}/schedule")
    public JobResponse reschedule(@PathVariable UUID id, @RequestParam Instant scheduledAt, Authentication auth) {
        return jobService.reschedule(id, scheduledAt, auth.getName());
    }
}
