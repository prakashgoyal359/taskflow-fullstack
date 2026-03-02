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
  @Output() activity = new EventEmitter<any>();

  comments: any[] = [];
  newComment = '';
  currentUser = '';

  isLoadingComments = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.currentUser = localStorage.getItem('name') || '';
    this.loadComments();
  }

  loadComments() {
    this.isLoadingComments = true;

    this.http.get(`http://localhost:8080/api/tasks/${this.task.id}/comments`).subscribe({
      next: (res: any) => {
        this.comments = res;
        this.isLoadingComments = false;
      },
      error: () => {
        this.isLoadingComments = false;
      },
    });
  }

  isPosting = false;

  postComment() {
    if (!this.newComment) return;

    this.activity.emit({
      actorName: localStorage.getItem('name'),
      message: 'commented on a task',
      actionType: 'COMMENT',
      createdAt: new Date(),
    });

    const tempComment = {
      body: this.newComment,
      author: {
        fullName: localStorage.getItem('name'),
      },
      createdAt: new Date(),
    };

    // 🔥 instantly show in UI
    this.comments.push(tempComment);

    this.http
      .post(`http://localhost:8080/api/tasks/${this.task.id}/comments`, {
        body: this.newComment,
      })
      .subscribe({
        next: () => {
          this.newComment = '';
        },
        error: () => {
          alert('Failed to post comment');
        },
      });
  }

  deleteComment(id: number) {
    // instant remove

    this.activity.emit({
      actorName: localStorage.getItem('name'),
      message: 'deleted a comment',
      actionType: 'COMMENT',
      createdAt: new Date(),
    });

    this.comments = this.comments.filter((c) => c.id !== id);

    this.http.delete(`http://localhost:8080/api/comments/${id}`).subscribe();
  }
}
