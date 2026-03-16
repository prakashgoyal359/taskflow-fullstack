import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AttachmentService {
  API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

 uploadFile(taskId:number, file:File){

  const formData = new FormData();

  formData.append("file", file);

  const token = localStorage.getItem("token");

  return this.http.post(
    `http://localhost:8080/api/tasks/${taskId}/attachments`,
    formData,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );

}

  getAttachments(taskId: number) {
    return this.http.get(`${this.API}/tasks/${taskId}/attachments`);
  }

  downloadFile(id: number) {
    return this.http.get(`${this.API}/attachments/${id}/download`, { responseType: 'blob' });
  }

  deleteFile(id: number) {
    return this.http.delete(`${this.API}/attachments/${id}`);
  }
}
