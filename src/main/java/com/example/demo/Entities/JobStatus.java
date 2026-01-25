package com.example.demo.Entities;

import jakarta.persistence.Entity;

public enum JobStatus {
    RUNNING,
    SCHEDULED,
    RESCHEDULED,
    COMPLETED,
    FAILED
}
