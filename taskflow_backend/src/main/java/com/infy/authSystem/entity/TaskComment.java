package com.infy.authSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TaskComment {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TASK
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    // AUTHOR
    @ManyToOne
    @JsonIgnoreProperties({"passwordHash"})
    private User author;

    @Column(columnDefinition = "TEXT")
    private String body;

    private LocalDateTime createdAt = LocalDateTime.now();
}