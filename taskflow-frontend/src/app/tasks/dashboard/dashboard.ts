import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task } from '../../services/task';
import { TaskForm } from '../task-form/task-form';
import { Navbar } from '../../shared/navbar/navbar';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TaskForm, Navbar],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  tasks: any[] = [];

  showForm = false;
  selectedTask: any = null;

  constructor(private taskService: Task) {}

  ngOnInit() {
    this.loadTasks();
  }

  // LOAD TASKS
  loadTasks() {
    this.taskService.getTasks().subscribe((res: any) => {
      this.tasks = res;
    });
  }

  // OPEN CREATE FORM
  openForm() {
    this.selectedTask = null;
    this.showForm = true;
  }

  // EDIT TASK
  editTask(task: any) {
    this.selectedTask = task;
    this.showForm = true;
  }

  // CLOSE FORM
  closeForm() {
    this.showForm = false;
  }

  // DELETE TASK
  deleteTask(id: number) {
    this.taskService.deleteTask(id).subscribe(() => {
      // remove from UI instantly
      this.tasks = this.tasks.filter((t) => t.id !== id);
    });
  }
}
