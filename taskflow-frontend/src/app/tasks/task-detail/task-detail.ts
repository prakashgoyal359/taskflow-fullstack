import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Task } from '../../services/task';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-detail.html',
})
export class TaskDetail implements OnInit {
  @Input() task: any;
  @Output() close = new EventEmitter<void>();

  comments: any[] = [];
  newComment = '';
  currentUser = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.currentUser = localStorage.getItem('name') || '';
    this.loadComments();
  }

  loadComments() {
    this.http
      .get(`http://localhost:8080/api/tasks/${this.task.id}/comments`)
      .subscribe((res: any) => (this.comments = res));
  }

  postComment() {
    if (!this.newComment) return;

    this.http
      .post(`http://localhost:8080/api/tasks/${this.task.id}/comments`, {
        body: this.newComment,
      })
      .subscribe(() => {
        this.newComment = '';
        this.loadComments();
      });
  }

  deleteComment(id: number) {
    this.http
      .delete(`http://localhost:8080/api/comments/${id}`)
      .subscribe(() => this.loadComments());
  }
}
