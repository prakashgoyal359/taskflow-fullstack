package com.infy.authSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.infy.authSystem.entity.Task;
import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.TaskRepository;
import com.infy.authSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
		this.taskRepo = taskRepo;
		this.userRepo = userRepo;
	}

	public List<Task> getTasks(String email){
        return taskRepo.findByUserEmail(email);
    }

    public Task createTask(Task task, String email){
        User user = userRepo.findByEmail(email).get();
        task.setUser(user);
        return taskRepo.save(task);
    }
    
    public Task updateTask(Long id, Task updatedTask, String email) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check task owner
        if (!existingTask.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to update this task");
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setStatus(updatedTask.getStatus());

        return taskRepo.save(existingTask);
    }
    
    public void deleteTask(Long id, String email) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this task");
        }

        taskRepo.delete(existingTask);
    }
}