package com.infy.authSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.authSystem.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserEmail(String email);
}