package com.infy.authSystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.infy.authSystem.dto.TaskSummaryDto;
import com.infy.authSystem.entity.Task;
import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.TaskRepository;
import com.infy.authSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ActivityService activityService;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public TaskService(ActivityService activityService, TaskRepository taskRepo, UserRepository userRepo) {
		this.activityService = activityService;
		this.taskRepo = taskRepo;
		this.userRepo = userRepo;
	}

	// 🔹 GET ALL TASKS
    public List<Task> getTasks(String email) {
        return taskRepo.findByUserEmail(email);
    }

    // 🔹 CREATE TASK
    public Task createTask(Task task, String email) {

        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(owner);

        // default priority
        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }

        // assignee
        if (task.getAssignee() != null && task.getAssignee().getId() != null) {
            User assignee = userRepo.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        // ✅ SAVE FIRST
        Task savedTask = taskRepo.save(task);

        // ✅ LOG AFTER SAVE
        activityService.log(
                "CREATE",
                owner.getFullName() + " created task " + savedTask.getTitle(),
                owner.getFullName(),
                savedTask.getId()
        );

        return savedTask;
    }

    // 🔹 UPDATE TASK
    public Task updateTask(Long id, Task updatedTask, String email) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingTask.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to update this task");
        }

        // 🔁 check changes BEFORE updating
        String oldStatus = existingTask.getStatus();
        String oldPriority = existingTask.getPriority();

        // update fields
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setStatus(updatedTask.getStatus());

        // priority
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }

        // assignee
        if (updatedTask.getAssignee() != null && updatedTask.getAssignee().getId() != null) {
            User assignee = userRepo.findById(updatedTask.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            existingTask.setAssignee(assignee);
        } else {
            existingTask.setAssignee(null);
        }

        // ✅ SAVE FIRST
        Task saved = taskRepo.save(existingTask);

        // 🔥 STATUS CHANGE LOG
        if (!oldStatus.equals(updatedTask.getStatus())) {
            activityService.log(
                    "STATUS",
                    user.getFullName() + " changed status of " + saved.getTitle() + " to " + updatedTask.getStatus(),
                    user.getFullName(),
                    saved.getId()
            );
        }

        // 🔥 PRIORITY CHANGE LOG
        if (updatedTask.getPriority() != null && !oldPriority.equals(updatedTask.getPriority())) {
            activityService.log(
                    "PRIORITY",
                    user.getFullName() + " changed priority of " + saved.getTitle() + " to " + updatedTask.getPriority(),
                    user.getFullName(),
                    saved.getId()
            );
        }

        return saved;
    }

    // 🔹 DELETE TASK
    public void deleteTask(Long id, String email) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingTask.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this task");
        }

        // log before delete
        activityService.log(
                "DELETE",
                user.getFullName() + " deleted task " + existingTask.getTitle(),
                user.getFullName(),
                null
        );

        taskRepo.delete(existingTask);
    }

    // 🔹 ANALYTICS SUMMARY
    public TaskSummaryDto getSummary(String email) {

        List<Task> tasks = taskRepo.findByUserEmail(email);

        TaskSummaryDto dto = new TaskSummaryDto();

        dto.setTotalTasks(tasks.size());

        dto.setTodo(tasks.stream().filter(t -> "TODO".equals(t.getStatus())).count());
        dto.setInProgress(tasks.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count());
        dto.setDone(tasks.stream().filter(t -> "DONE".equals(t.getStatus())).count());

        LocalDate today = LocalDate.now();

        dto.setOverdue(tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(today) && !"DONE".equals(t.getStatus()))
                .count());

        dto.setDueToday(tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isEqual(today))
                .count());

        dto.setThisWeek(tasks.stream()
                .filter(t -> t.getDueDate() != null &&
                        !t.getDueDate().isBefore(today) &&
                        !t.getDueDate().isAfter(today.plusDays(7)))
                .count());

        dto.setHigh(tasks.stream().filter(t -> "HIGH".equals(t.getPriority())).count());
        dto.setMedium(tasks.stream().filter(t -> "MEDIUM".equals(t.getPriority())).count());
        dto.setLow(tasks.stream().filter(t -> "LOW".equals(t.getPriority())).count());

        if (dto.getTotalTasks() > 0) {
            dto.setCompletionRate((dto.getDone() * 100.0) / dto.getTotalTasks());
        }

        return dto;
    }
}