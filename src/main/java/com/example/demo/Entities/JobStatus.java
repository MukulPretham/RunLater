package com.example.demo.Entities;

import jakarta.persistence.Entity;

public enum JobStatus {
    PROCESSING,
    SCHEDULED,
    RETRYING,
    COMPLETED,
    FAILED,
    CANCELLED
}
