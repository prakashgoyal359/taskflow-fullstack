package com.infy.authSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.infy.authSystem.entity.ActivityLog;
import com.infy.authSystem.repository.ActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    public ActivityService(ActivityRepository repo) {
		this.repo = repo;
	}

	private final ActivityRepository repo;

    public void log(String actionType, String message, String actorName, Long taskId) {

        ActivityLog log = new ActivityLog();
        log.setActionType(actionType);
        log.setMessage(message);
        log.setActorName(actorName);
        log.setTaskId(taskId);
        log.setCreatedAt(LocalDateTime.now());

        repo.save(log);
    }

    public List<ActivityLog> getRecent() {
        return repo.findTop20ByOrderByCreatedAtDesc();
    }
}