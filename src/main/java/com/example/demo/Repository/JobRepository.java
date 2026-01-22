package com.example.demo.Repository;

import com.example.demo.Entities.Job;
import com.example.demo.Entities.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Jobs,Long> {
    Jobs findByType(Job job);
}
