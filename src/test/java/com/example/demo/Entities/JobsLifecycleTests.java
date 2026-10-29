package com.example.demo.Entities;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JobsLifecycleTests {

    @Test
    void failedDeliveryIsRequeuedWithBackoffUntilAttemptsAreExhausted() {
        Jobs job = new Jobs(new Users("alice", "encoded"), Job.WEBHOOK, 2, Instant.now(), null);

        job.markProcessing();
        job.markFailed("endpoint unavailable");

        assertEquals(JobStatus.RETRYING, job.getStatus());
        assertEquals(1, job.getAttemptCount());
        assertNotNull(job.getNextAttemptAt());
        assertEquals("endpoint unavailable", job.getLastError());

        job.markProcessing();
        job.markFailed("endpoint unavailable");

        assertEquals(JobStatus.FAILED, job.getStatus());
        assertEquals(2, job.getAttemptCount());
        assertNotNull(job.getCompletedAt());
    }

    @Test
    void manualRetryResetsTheAttemptBudget() {
        Jobs job = new Jobs(new Users("alice", "encoded"), Job.EMAIL, 1, Instant.now(), null);
        job.markProcessing();
        job.markFailed("mail provider unavailable");

        assertTrue(job.retryNow());
        assertEquals(JobStatus.RETRYING, job.getStatus());
        assertEquals(0, job.getAttemptCount());
        assertNull(job.getLastError());
    }
}
