package com.infy.authSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.authSystem.entity.ActivityLog;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop20ByOrderByCreatedAtDesc();

}
