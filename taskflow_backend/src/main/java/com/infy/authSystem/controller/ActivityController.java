package com.infy.authSystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.infy.authSystem.entity.ActivityLog;
import com.infy.authSystem.service.ActivityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public List<ActivityLog> getFeed() {
        return activityService.getRecent();
    }

	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}
}