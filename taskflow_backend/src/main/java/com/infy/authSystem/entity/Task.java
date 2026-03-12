package com.infy.authSystem.entity;

import java.time.LocalDate;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String description;
	private LocalDate dueDate;
	private String status;
	
	private String priority; // HIGH / MEDIUM / LOW
	
	@ManyToOne
	@JoinColumn(name = "assigned_to")
	private User assignee;

	@ManyToOne
	private User user;

	@ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
	
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskComment> comments;

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<TaskComment> getComments() {
		return comments;
	}

	public void setComments(List<TaskComment> comments) {
		this.comments = comments;
	}
}
