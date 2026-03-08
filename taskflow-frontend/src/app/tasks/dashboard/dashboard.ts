import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task } from '../../services/task';
import { TaskForm } from '../task-form/task-form';
import { Navbar } from '../../shared/navbar/navbar';
import { ChangeDetectorRef } from '@angular/core';
import { TaskDetail } from '../task-detail/task-detail';
import { TaskDueDatePipe } from '../../pipes/task-due-date/task-due-date';
import { Router } from '@angular/router';

declare var Chart: any;

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TaskForm, Navbar, TaskDetail, TaskDueDatePipe],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  tasks: any[] = [];
  filteredTasks: any[] = [];

  showForm = false;
  selectedTask: any = null;

  showAnalytics = false;
  summary: any = null;
  analyticsLoaded = false;

  activeTab = 'ALL';

  activityFeed: any[] = [];
  loadingFeed = false;

  overdueCount = 0;
  dueTodayCount = 0;
  showBanner = true;
  today = new Date().toISOString().split('T')[0];

  constructor(
    private taskService: Task,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit() {
    const token = localStorage.getItem('token');

    // ❌ No token → redirect to login
    if (!token) {
      this.router.navigate(['/login'], { replaceUrl: true });
      return;
    }

    try {
      // 🔓 Decode JWT
      const payload = JSON.parse(atob(token.split('.')[1]));

      const expiry = payload.exp * 1000;

      // ❌ Token expired
      if (Date.now() > expiry) {
        console.warn('JWT expired');

        localStorage.removeItem('token');
        localStorage.removeItem('name');

        this.router.navigate(['/login'], { replaceUrl: true });

        return;
      }
    } catch (err) {
      // ❌ Invalid token
      localStorage.removeItem('token');
      localStorage.removeItem('name');

      this.router.navigate(['/login'], { replaceUrl: true });

      return;
    }

    setTimeout(() => {
      this.loadTasks();
      this.loadFeed();
    });
  }

  loading = false;

  loadTasks() {
    this.taskService.getTasks().subscribe((res: any) => {
      this.tasks = res;
      this.filterTasks(this.activeTab);
      this.calculateDueDateAlerts();
      this.sortByPriority();
      this.cdr.detectChanges();
      this.loadFeed();
    });
  }

  addActivity(activity: any) {
    if (!this.activityFeed) {
      this.activityFeed = [];
    }

    this.activityFeed.unshift(activity);

    // keep only latest 20 items
    if (this.activityFeed.length > 20) {
      this.activityFeed.pop();
    }
  }

  toggleAnalytics() {
    this.showAnalytics = !this.showAnalytics;

    // if opening panel
    if (this.showAnalytics) {
      // load summary only once
      if (!this.summaryLoaded) {
        this.loadSummary();
      }

      // wait for DOM to render canvas
      setTimeout(() => {
        this.renderCharts();
      }, 200);
    }
  }

  summaryLoaded = false;

  loadSummary() {
    this.taskService.getSummary().subscribe((res: any) => {
      this.summary = res;
      this.summaryLoaded = true;
    });
  }

  loadAnalytics() {
    this.taskService.getSummary().subscribe((res: any) => {
      this.summary = res;
      this.analyticsLoaded = true;

      this.renderCharts();

      setTimeout(() => {
        this.renderCharts();
      }, 200);
    });
  }

  statusChart: any;
  priorityChart: any;

  renderCharts() {
    // ❌ destroy old charts first
    if (this.statusChart) this.statusChart.destroy();
    if (this.priorityChart) this.priorityChart.destroy();

    const statusCtx = document.getElementById('statusChart') as HTMLCanvasElement;
    const priorityCtx = document.getElementById('priorityChart') as HTMLCanvasElement;

    if (!statusCtx || !priorityCtx) return;

    // 🔵 STATUS CHART
    this.statusChart = new Chart(statusCtx, {
      type: 'doughnut',
      data: {
        labels: ['To-Do', 'In Progress', 'Done'],
        datasets: [
          {
            data: [this.summary.todo, this.summary.inProgress, this.summary.done],
            backgroundColor: ['#3b82f6', '#f59e0b', '#22c55e'],
          },
        ],
      },
    });

    // 🔴 PRIORITY CHART
    this.priorityChart = new Chart(priorityCtx, {
      type: 'bar',
      data: {
        labels: ['High', 'Medium', 'Low'],
        datasets: [
          {
            data: [this.summary.high, this.summary.medium, this.summary.low],
            backgroundColor: ['#ef4444', '#f59e0b', '#22c55e'],
          },
        ],
      },
    });
  }

  filterTasks(status: string) {
    this.activeTab = status;

    const currentUser = localStorage.getItem('name');

    if (status === 'ALL') {
      this.filteredTasks = [...this.tasks];
    } else if (status === 'ASSIGNED') {
      this.filteredTasks = this.tasks.filter(
        (t) => t.assignee && t.assignee.fullName === currentUser,
      );
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

  loadFeed(forceRefresh: boolean = false) {
    if (this.loadingFeed && !forceRefresh) return;

    // 🔥 FIX: wrap in setTimeout so Angular change cycle is safe
    setTimeout(() => {
      this.loadingFeed = true;
    });

    this.taskService.getActivityFeed().subscribe({
      next: (res: any) => {
        // 🔥 assign data safely
        this.activityFeed = res || [];

        // 🔥 fix change detection error
        setTimeout(() => {
          this.loadingFeed = false;
        });
      },
      error: (err) => {
        console.error(err);

        setTimeout(() => {
          this.loadingFeed = false;
        });
      },
    });
  }

  sortByPriority() {
    const priorityOrder: any = {
      HIGH: 1,
      MEDIUM: 2,
      LOW: 3,
    };

    this.filteredTasks.sort((a: any, b: any) => {
      // 1️⃣ sort by priority
      const p1 = priorityOrder[a.priority] || 4;
      const p2 = priorityOrder[b.priority] || 4;

      if (p1 !== p2) {
        return p1 - p2;
      }

      // 2️⃣ secondary sort by due date (earliest first)
      const d1 = new Date(a.dueDate).getTime();
      const d2 = new Date(b.dueDate).getTime();

      return d1 - d2;
    });
  }

  calculateDueDateAlerts() {
    const today = new Date().toISOString().split('T')[0];

    this.overdueCount = this.tasks.filter(
      (t: any) => t.dueDate && t.dueDate < today && t.status !== 'DONE',
    ).length;

    this.dueTodayCount = this.tasks.filter(
      (t: any) => t.dueDate === today && t.status !== 'DONE',
    ).length;
  }
}
