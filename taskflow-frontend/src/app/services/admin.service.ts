import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AdminService {
  API = 'http://localhost:8080/api/admin/users';

  constructor(private http: HttpClient) {}

  getUsers() {
    return this.http.get(this.API);
  }

  updateRole(id: number, role: string) {
    return this.http.patch(`${this.API}/${id}/role`, { role: role });
  }

  toggleUser(id: number) {
    return this.http.patch(`${this.API}/${id}/status`, {});
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.API}/${id}`);
  }
}
