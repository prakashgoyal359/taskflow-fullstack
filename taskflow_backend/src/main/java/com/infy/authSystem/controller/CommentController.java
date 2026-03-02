package com.infy.authSystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.infy.authSystem.entity.TaskComment;
import com.infy.authSystem.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}

	private final CommentService commentService;

    // GET COMMENTS
    @GetMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<List<TaskComment>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getComments(taskId));
    }

    // ADD COMMENT
    @PostMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<TaskComment> addComment(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        TaskComment comment = commentService.addComment(
                taskId,
                body.get("body"),
                auth.getName()
        );

        return ResponseEntity.ok(comment);
    }

    // DELETE COMMENT
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, Authentication auth) {
        commentService.deleteComment(id, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}