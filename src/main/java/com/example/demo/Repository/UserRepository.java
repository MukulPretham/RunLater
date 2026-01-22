package com.example.demo.Repository;

import com.example.demo.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByUsername(String username);
    Users findByUsername(String name);
}
