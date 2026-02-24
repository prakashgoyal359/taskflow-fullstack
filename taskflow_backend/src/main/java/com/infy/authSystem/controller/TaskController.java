package com.infy.authSystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.infy.authSystem.entity.Task;
import com.infy.authSystem.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")   // allow Angular
public class TaskController {

    public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	private final TaskService taskService;

    // 🔹 GET ALL TASKS
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication auth){
        List<Task> tasks = taskService.getTasks(auth.getName());
        return ResponseEntity.ok(tasks);
    }

    // 🔹 CREATE TASK
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication auth){
        Task created = taskService.createTask(task, auth.getName());
        return ResponseEntity.ok(created);
    }

    // 🔹 UPDATE TASK
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                        @RequestBody Task task,
                                        Authentication auth) {

        Task updated = taskService.updateTask(id, task, auth.getName());

        return ResponseEntity.ok(
            Map.of(
                "message", "Task updated successfully",
                "task", updated
            )
        );
    }

    // 🔹 DELETE TASK (IMPORTANT FIX)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id,
                                        Authentication auth) {

        taskService.deleteTask(id, auth.getName());

        // ✅ return JSON instead of plain text
        return ResponseEntity.ok(
            Map.of("message", "Task deleted successfully")
        );
    }
}