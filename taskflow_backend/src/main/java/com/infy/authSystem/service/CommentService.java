package com.infy.authSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    private final TaskCommentRepository commentRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public CommentService(TaskCommentRepository commentRepo, TaskRepository taskRepo, UserRepository userRepo) {
		super();
		this.commentRepo = commentRepo;
		this.taskRepo = taskRepo;
		this.userRepo = userRepo;
	}

	// GET COMMENTS
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

        return commentRepo.save(comment);
    }

    // DELETE COMMENT
    public void deleteComment(Long id, String email) {

        TaskComment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Not allowed to delete this comment");
        }

        commentRepo.delete(comment);
    }
}