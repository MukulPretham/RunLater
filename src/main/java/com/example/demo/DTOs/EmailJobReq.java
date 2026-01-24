package com.example.demo.DTOs;

import com.example.demo.Entities.Job;

import java.time.Instant;

public record EmailJobReq(
        Job type,
        int retryCount,
        Instant scheduledAt,

        String from,
        String [] to,
        String subject,
        String body,
        String contentType
) {}