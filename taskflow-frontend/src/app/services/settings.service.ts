import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SettingsService {
  API = 'http://localhost:8080/api/users/me';

  constructor(private http: HttpClient) {}

  // GET PROFILE
  getProfile(): Observable<any> {
    return this.http.get(this.API);
  }

  // UPDATE PROFILE
  updateProfile(data: any): Observable<any> {
    return this.http.patch(`${this.API}/profile`, data);
  }
}
