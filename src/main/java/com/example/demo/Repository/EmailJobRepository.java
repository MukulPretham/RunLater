package com.example.demo.Repository;

import com.example.demo.Entities.EmailJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJobRepository extends JpaRepository<EmailJobs,Long> {

}
