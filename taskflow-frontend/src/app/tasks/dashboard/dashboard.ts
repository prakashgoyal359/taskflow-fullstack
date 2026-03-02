import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task } from '../../services/task';
import { TaskForm } from '../task-form/task-form';
import { Navbar } from '../../shared/navbar/navbar';
import { ChangeDetectorRef } from '@angular/core';
import { TaskDetail } from '../task-detail/task-detail';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TaskForm, Navbar, TaskDetail],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  tasks: any[] = [];
  filteredTasks: any[] = [];

  showForm = false;
  selectedTask: any = null;

  activeTab = 'ALL';

  constructor(
    private taskService: Task,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadTasks();
  }

  loading = false;

  loadTasks() {
    this.taskService.getTasks().subscribe((res: any) => {
      this.tasks = res;
      this.filterTasks(this.activeTab);
      this.cdr.detectChanges();
    });
  }

  filterTasks(status: string) {
    this.activeTab = status;

    if (status === 'ALL') {
      this.filteredTasks = [...this.tasks]; // 🔥 important copy
    } else {
      this.filteredTasks = this.tasks.filter((t) => t.status === status);
    }
  }

  getCount(status: string) {
    return this.tasks.filter((t) => t.status === status).length;
  }

  openForm() {
    this.selectedTask = null;
    this.showForm = true;
  }

  editTask(t: any) {
    this.selectedTask = t;
    this.showForm = true;
  }

  closeForm() {
    this.showForm = false;
  }

  deleteTask(id: number) {
    // 🔴 confirmation popup
    const confirmDelete = confirm('Are you sure you want to delete this task?');

    // ❌ if user cancels
    if (!confirmDelete) return;

    // ✅ if user confirms
    this.taskService.deleteTask(id).subscribe({
      next: () => {
        this.loadTasks(); // reload tasks
      },
      error: (err) => console.error(err),
    });
  }

  showDetail = false;

  viewTask(task: any) {
    this.selectedTask = task;
    this.showDetail = true;
  }
}
