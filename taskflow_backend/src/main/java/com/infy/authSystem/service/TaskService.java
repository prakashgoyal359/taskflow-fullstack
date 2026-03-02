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

    // 🔹 GET ALL TASKS OF LOGGED USER
    public List<Task> getTasks(String email) {
        return taskRepo.findByUserEmail(email);
    }

    // 🔹 CREATE TASK (UPDATED)
    public Task createTask(Task task, String email) {

        // set owner
        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(owner);

        // 🔥 set PRIORITY (default MEDIUM if null)
        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }

        // 🔥 set ASSIGNEE if provided
        if (task.getAssignee() != null && task.getAssignee().getId() != null) {

            User assignee = userRepo.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));

            task.setAssignee(assignee);
        } else {
            task.setAssignee(null); // unassigned
        }

        return taskRepo.save(task);
    }

    // 🔹 UPDATE TASK (UPDATED)
    public Task updateTask(Long id, Task updatedTask, String email) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // 🔐 Check owner
        if (!existingTask.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to update this task");
        }

        // 🔄 update fields
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setStatus(updatedTask.getStatus());

        // 🔥 update PRIORITY
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }

        // 🔥 update ASSIGNEE
        if (updatedTask.getAssignee() != null && updatedTask.getAssignee().getId() != null) {

            User assignee = userRepo.findById(updatedTask.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));

            existingTask.setAssignee(assignee);
        } else {
            existingTask.setAssignee(null); // remove assignment
        }

        return taskRepo.save(existingTask);
    }

    // 🔹 DELETE TASK
    public void deleteTask(Long id, String email) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this task");
        }

        taskRepo.delete(existingTask);
    }

	public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
		super();
		this.taskRepo = taskRepo;
		this.userRepo = userRepo;
	}
}