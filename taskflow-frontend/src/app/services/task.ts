import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class Task {
  API = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  // GET ALL TASKS
  getTasks() {
    return this.http.get(this.API);
  }

  getUsers() {
    return this.http.get('http://localhost:8080/api/users');
  }

  // CREATE TASK
  createTask(data: any) {
    return this.http.post(this.API, data);
  }

  // UPDATE TASK
  updateTask(id: number, data: any) {
    return this.http.put(`${this.API}/${id}`, data);
  }

  // DELETE TASK
  deleteTask(id: number) {
    return this.http.delete(`${this.API}/${id}`);
  }

  getSummary() {
    return this.http.get('http://localhost:8080/api/tasks/summary');
  }

  getActivityFeed() {
    return this.http.get('http://localhost:8080/api/activity');
  }
}
