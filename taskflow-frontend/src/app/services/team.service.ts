import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class TeamService {
  API = 'http://localhost:8080/api/teams';
  USERS = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // GET TEAMS
  getTeams() {
    return this.http.get(this.API);
  }

  // CREATE TEAM
  createTeam(data: any) {
    return this.http.post(this.API, data);
  }

  // DELETE TEAM
  deleteTeam(id: number) {
    return this.http.delete(`${this.API}/${id}`);
  }

  // ADD MEMBER
  addMember(teamId: number, userId: number) {
    return this.http.post(`${this.API}/${teamId}/members/${userId}`, {});
  }

  // REMOVE MEMBER
  removeMember(teamId: number, userId: number) {
    return this.http.delete(`${this.API}/${teamId}/members/${userId}`);
  }

  // GET USERS
  getUsers() {
    return this.http.get(this.USERS);
  }
}
