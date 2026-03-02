package com.infy.authSystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.infy.authSystem.entity.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskIdOrderByCreatedAtAsc(Long taskId);
}