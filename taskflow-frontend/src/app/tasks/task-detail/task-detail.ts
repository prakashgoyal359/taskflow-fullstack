import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentService } from '../../services/comment';
import { AttachmentsComponent } from '../../attachments/attachments.component';
@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, AttachmentsComponent],
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

    const body = this.newComment;

    this.commentService.addComment(this.task.id, body).subscribe({
      next: (savedComment: any) => {
        this.comments.push(savedComment);
        this.newComment = '';

        // 🔥 ACTIVITY FEED ENTRY
        this.activity.emit({
          actorName: localStorage.getItem('name'),
          message: `${localStorage.getItem('name')} commented on ${this.task.title}`,
          actionType: 'COMMENT',
          createdAt: new Date(),
        });
      },

      error: () => {
        alert('Failed to post comment');
      },
    });
  }

  deleteComment(id: number) {
    if (!id) return;

    const original = [...this.comments];

    this.comments = this.comments.filter((c) => c.id !== id);

    this.commentService.deleteComment(id).subscribe({
      next: () => {},

      error: (err) => {
        if (err.status === 403) {
          alert("You cannot delete another user's comment.");

          this.comments = original;
        }
      },
    });
  }
}
