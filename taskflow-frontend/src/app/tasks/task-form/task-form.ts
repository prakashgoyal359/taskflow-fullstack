import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Task } from '../../services/task';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './task-form.html',
})
export class TaskForm {
  @Input() task: any = null;

  @Output() close = new EventEmitter<void>();
  @Output() refresh = new EventEmitter<void>();

  form: any = {
    title: '',
    description: '',
    dueDate: '',
    status: 'TODO',
    priority: 'MEDIUM',
    assignedTo: null,
  };

  users: any[] = [];
  selectedUser: any = null;

  // 🔴 validation flags
  titleError = false;
  descError = false;
  dateError = false;

  constructor(private taskService: Task) {}

  ngOnInit() {
    this.loadUsers();

    if (this.task) {
      this.form = { ...this.task };

      this.selectedUser = this.task.assignee || null;
    }
  }

  loadUsers() {
    this.taskService.getUsers().subscribe((res: any) => {
      this.users = res;
    });
  }

  // 🔐 validate required fields
  validateForm() {
    this.titleError = !this.form.title;
    this.descError = !this.form.description;
    this.dateError = !this.form.dueDate;

    return !(this.titleError || this.descError || this.dateError);
  }

  // 💾 SAVE / UPDATE
  save() {
    if (!this.validateForm()) return;

    this.form.assignee = this.selectedUser ? { id: this.selectedUser.id } : null;

    if (!this.form.priority) {
      this.form.priority = 'MEDIUM';
    }

    if (this.task) {
      // 🔁 UPDATE
      this.taskService.updateTask(this.task.id, this.form).subscribe(() => {
        this.refresh.emit();
        this.close.emit();
      });
    } else {
      // ➕ CREATE
      this.taskService.createTask(this.form).subscribe(() => {
        this.refresh.emit();
        this.close.emit();
      });
    }
  }

  // 🔴 DELETE (Danger Zone)
  confirmDelete() {
    const confirmBox = confirm(
      '⚠ Are you sure you want to delete this task? This action cannot be undone.',
    );

    if (!confirmBox) return;

    this.taskService.deleteTask(this.task.id).subscribe(() => {
      this.refresh.emit();
      this.close.emit();
    });
  }
}
