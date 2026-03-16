import { Component, Input, OnInit } from '@angular/core';
import { AttachmentService } from '../services/attachment.service';
import { DecimalPipe } from '@angular/common';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-attachments',
  templateUrl: './attachments.component.html',
  imports: [DecimalPipe, CommonModule],
})
export class AttachmentsComponent implements OnInit {
  @Input() taskId!: number;

  attachments: any[] = [];

  uploading = false;

  constructor(private service: AttachmentService) {}

  ngOnInit(): void {
    this.loadFiles();
  }

  loadFiles() {
    this.service.getAttachments(this.taskId).subscribe((data: any) => {
      this.attachments = data;
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];

    if (file.size > 5 * 1024 * 1024) {
      alert('File exceeds 5MB limit');
      return;
    }

    this.uploading = true;

    this.service.uploadFile(this.taskId, file).subscribe(() => {
      this.uploading = false;
      this.loadFiles();
    });
  }

  download(id: number, name: string) {
    this.service.downloadFile(id).subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');

      a.href = url;
      a.download = name;

      a.click();
    });
  }

  delete(id: number) {
    this.service.deleteFile(id).subscribe(() => {
      this.loadFiles();
    });
  }
}
