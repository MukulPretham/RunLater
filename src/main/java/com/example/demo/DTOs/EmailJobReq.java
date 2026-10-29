package com.example.demo.DTOs;

import java.time.Instant;
import jakarta.validation.constraints.*;

public record EmailJobReq(
        @Min(1) @Max(10) int maxAttempts,
        Instant scheduledAt,
        @Email String from,
        @NotEmpty String[] to,
        @NotBlank @Size(max = 255) String subject,
        @NotBlank String body,
        @NotBlank @Pattern(regexp = "text/plain|text/html") String contentType
) {}
