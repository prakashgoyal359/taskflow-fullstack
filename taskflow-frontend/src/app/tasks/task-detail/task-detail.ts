import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentService } from '../../services/comment';

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

  constructor(private commentService: CommentService) {}

  ngOnInit() {
    this.currentUser = localStorage.getItem('name') || '';
    this.loadComments();
  }

  loadComments() {
    this.isLoadingComments = true;

    this.commentService.getComments(this.task.id).subscribe({
      next: (res: any) => {
        this.comments = res;
        this.isLoadingComments = false;
      },
      error: () => {
        this.isLoadingComments = false;
      },
    });
  }

  postComment() {
    if (!this.newComment) return;

    const tempComment = {
      body: this.newComment,
      author: { fullName: this.currentUser },
      createdAt: new Date(),
    };

    // instant UI update
    this.comments.push(tempComment);

    this.commentService.addComment(this.task.id, this.newComment).subscribe({
      next: () => {
        this.newComment = '';
      },
      error: () => {
        alert('Failed to post comment');
      },
    });
  }

  deleteComment(id: number) {
    const original = [...this.comments];

    // remove instantly
    this.comments = this.comments.filter((c) => c.id !== id);

    this.commentService.deleteComment(id).subscribe({
      next: () => {},

      error: (err) => {
        // restore if forbidden
        if (err.status === 403) {
          alert("You cannot delete another user's comment.");

          this.comments = original;
        }
      },
    });
  }
}
