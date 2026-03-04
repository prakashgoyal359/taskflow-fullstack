import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class Task {
  API = 'http://localhost:8080/api/tasks';
  USERS = 'http://localhost:8080/api/users';
  ACTIVITY = 'http://localhost:8080/api/activity';

  constructor(private http: HttpClient) {}

  // GET ALL TASKS (GLOBAL)
  getTasks() {
    return this.http.get(this.API);
  }

  // GET USERS (FOR ASSIGN)
  getUsers() {
    return this.http.get(this.USERS);
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

  // ANALYTICS
  getSummary() {
    return this.http.get(`${this.API}/summary`);
  }

  // ACTIVITY FEED
  getActivityFeed() {
    return this.http.get(this.ACTIVITY);
  }
}
