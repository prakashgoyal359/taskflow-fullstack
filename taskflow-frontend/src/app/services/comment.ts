import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class CommentService {
  API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // GET COMMENTS (GLOBAL)
  getComments(taskId: number) {
    return this.http.get(`${this.API}/tasks/${taskId}/comments`);
  }

  // ADD COMMENT
  addComment(taskId: number, body: string) {
    return this.http.post(`${this.API}/tasks/${taskId}/comments`, {
      body: body,
    });
  }

  // DELETE COMMENT
  deleteComment(id: number) {
    return this.http.delete(`${this.API}/comments/${id}`);
  }
}
