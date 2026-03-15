package com.infy.authSystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.infy.authSystem.dto.TaskSummaryDto;
import com.infy.authSystem.entity.Role;
import com.infy.authSystem.entity.Task;
import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.TaskRepository;
import com.infy.authSystem.repository.UserRepository;

import jakarta.transaction.Transactional;
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

	// GET TASKS
    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    // CREATE TASK
    public Task createTask(Task task, String email) {

        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(owner);

        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }

        if (task.getAssignee() != null && task.getAssignee().getId() != null) {

            User assignee = userRepo.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));

            task.setAssignee(assignee);
        }

        Task saved = taskRepo.save(task);

        activityService.log(
                "CREATE",
                owner.getFullName() + " created task " + saved.getTitle(),
                owner.getFullName(),
                saved.getId()
        );

        return saved;
    }

    // UPDATE TASK
    public Task updateTask(Long id, Task updatedTask, String email) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());

        Task saved = taskRepo.save(task);

        activityService.log(
                "UPDATE",
                user.getFullName() + " updated task " + saved.getTitle(),
                user.getFullName(),
                saved.getId()
        );

        return saved;
    }

    // DELETE TASK
    @Transactional
    public void deleteTask(Long id, String email) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ADMIN can delete everything
        if(user.getRole() == Role.ADMIN){
            taskRepo.delete(task);
            return;
        }

        // Owner can delete their own task
        if(task.getUser().getEmail().equals(email)){
            taskRepo.delete(task);
            return;
        }

        throw new RuntimeException("You are not allowed to delete this task");
    }

    // ANALYTICS
    public TaskSummaryDto getSummary() {

        List<Task> tasks = taskRepo.findAll();

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