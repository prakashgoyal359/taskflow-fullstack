package com.infy.authSystem.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.infy.authSystem.entity.Task;
import com.infy.authSystem.entity.TaskComment;
import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.TaskCommentRepository;
import com.infy.authSystem.repository.TaskRepository;
import com.infy.authSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ActivityService activityService;

    public CommentService(TaskCommentRepository commentRepo, TaskRepository taskRepo, UserRepository userRepo, ActivityService activityService) {
		this.commentRepo = commentRepo;
		this.taskRepo = taskRepo;
		this.userRepo = userRepo;
		this.activityService = activityService;
	}

	private final TaskCommentRepository commentRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    // GET COMMENTS (GLOBAL)
    public List<TaskComment> getComments(Long taskId) {
        return commentRepo.findByTaskIdOrderByCreatedAtAsc(taskId);
    }

    // ADD COMMENT
    public TaskComment addComment(Long taskId, String body, String email) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setAuthor(user);
        comment.setBody(body);

        TaskComment saved = commentRepo.save(comment);

        // 🔥 LOG COMMENT ACTIVITY
        activityService.log(
                "COMMENT",
                user.getFullName() + " commented on " + task.getTitle(),
                user.getFullName(),
                task.getId()
        );

        return saved;
    }
    // DELETE COMMENT (AUTHOR ONLY)
    public void deleteComment(Long id, String email) {

        TaskComment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete another user's comment"
            );
        }

        commentRepo.delete(comment);
    }
}