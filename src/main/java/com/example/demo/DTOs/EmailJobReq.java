package com.example.demo.DTOs;

import com.example.demo.Entities.Job;

public record EmailJobReq(
        Job type,
        int retryCount,

        String from,
        String to,
        String subject,
        String body,
        String contentType
) {}