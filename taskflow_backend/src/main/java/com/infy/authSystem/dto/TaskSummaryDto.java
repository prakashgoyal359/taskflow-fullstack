package com.infy.authSystem.dto;

import lombok.Data;

@Data
public class TaskSummaryDto {

    private long totalTasks;

    private long todo;
    private long inProgress;
    public long getTotalTasks() {
		return totalTasks;
	}

	public TaskSummaryDto() {
			
	}

	public void setTotalTasks(long totalTasks) {
		this.totalTasks = totalTasks;
	}

	public long getTodo() {
		return todo;
	}

	public void setTodo(long todo) {
		this.todo = todo;
	}

	public long getInProgress() {
		return inProgress;
	}

	public void setInProgress(long inProgress) {
		this.inProgress = inProgress;
	}

	public long getDone() {
		return done;
	}

	public void setDone(long done) {
		this.done = done;
	}

	public long getOverdue() {
		return overdue;
	}

	public void setOverdue(long overdue) {
		this.overdue = overdue;
	}

	public long getDueToday() {
		return dueToday;
	}

	public void setDueToday(long dueToday) {
		this.dueToday = dueToday;
	}

	public long getThisWeek() {
		return thisWeek;
	}

	public void setThisWeek(long thisWeek) {
		this.thisWeek = thisWeek;
	}

	public long getHigh() {
		return high;
	}

	public void setHigh(long high) {
		this.high = high;
	}

	public long getMedium() {
		return medium;
	}

	public void setMedium(long medium) {
		this.medium = medium;
	}

	public long getLow() {
		return low;
	}

	public void setLow(long low) {
		this.low = low;
	}

	public double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(double completionRate) {
		this.completionRate = completionRate;
	}

	private long done;

    private long overdue;
    private long dueToday;
    private long thisWeek;

    private long high;
    private long medium;
    private long low;

    private double completionRate;
}