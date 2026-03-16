import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AdminService {
  API = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  // GET ALL USERS
  getUsers() {
    return this.http.get(`${this.API}/users`);
  }

  // UPDATE USER ROLE
  updateRole(id: number, role: string) {
    return this.http.patch(`${this.API}/users/${id}/role`, {
      role: role,
    });
  }

  // ACTIVATE / DEACTIVATE USER
  toggleUser(id: number) {
    return this.http.patch(`${this.API}/users/${id}/status`, {});
  }

  // DELETE USER
  deleteUser(id: number) {
    return this.http.delete(`${this.API}/users/${id}`);
  }
}
