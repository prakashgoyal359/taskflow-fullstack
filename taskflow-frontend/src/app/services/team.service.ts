import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class TeamService {
  API = 'http://localhost:8080/api/teams';
  USERS = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getTeams() {
    return this.http.get(this.API);
  }

  createTeam(data: any) {
    return this.http.post(this.API, data);
  }

  deleteTeam(id: number) {
    return this.http.delete(`${this.API}/${id}`);
  }

  addMember(teamId: number, userId: number) {
    return this.http.post(`${this.API}/${teamId}/members`, { userId: userId });
  }

  removeMember(teamId: number, userId: number) {
    return this.http.delete(`${this.API}/${teamId}/members/${userId}`);
  }

  getUsers() {
    return this.http.get(this.USERS);
  }
}
