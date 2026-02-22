package com.infy.authSystem.controller;

import java.util.List;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.authSystem.entity.Task;
import com.infy.authSystem.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
		super();
		this.taskService = taskService;
	}

	@GetMapping
    public List<Task> getTasks(Authentication auth){
        return taskService.getTasks(auth.getName());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication auth){
        return taskService.createTask(task, auth.getName());
    }
    
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody Task task,
                           Authentication authentication) {

        return taskService.updateTask(id, task, authentication.getName());
    }
    
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id,
                             Authentication authentication) {

        taskService.deleteTask(id, authentication.getName());
        return "Task deleted successfully";
    }
}
