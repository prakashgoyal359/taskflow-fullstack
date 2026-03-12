import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {
  API = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(data: any) {
    return this.http.post(`${this.API}/register`, data);
  }

  login(data: any) {
    return this.http.post(`${this.API}/login`, data);
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);

    const payload = JSON.parse(atob(token.split('.')[1]));

    localStorage.setItem('role', payload.role);
    localStorage.setItem('name', payload.fullName);
    localStorage.setItem('email', payload.sub);
  }

  logout() {
    localStorage.clear();
  }
}
