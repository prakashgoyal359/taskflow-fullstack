package com.infy.authSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.infy.authSystem.entity.ActivityLog;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop20ByOrderByCreatedAtDesc();
    @Modifying
    @Query("UPDATE ActivityLog a SET a.taskId = null WHERE a.taskId = :taskId")
    void detachTaskFromActivities(Long taskId);
}
