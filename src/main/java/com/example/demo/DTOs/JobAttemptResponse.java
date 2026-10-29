package com.example.demo.DTOs;

import java.time.Instant;

public record JobAttemptResponse(int attemptNumber, Instant attemptedAt, boolean successful, String detail) { }
