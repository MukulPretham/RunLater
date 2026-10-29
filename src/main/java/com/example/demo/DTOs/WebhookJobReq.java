package com.example.demo.DTOs;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record WebhookJobReq(
        @NotBlank @Pattern(regexp = "https?://.+") String targetUrl,
        @NotBlank String payload,
        @Size(max = 255) String secret,
        @Min(1) @Max(10) int maxAttempts,
        Instant scheduledAt
) { }
