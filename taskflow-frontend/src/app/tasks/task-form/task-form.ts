import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../../services/task';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './task-form.html',
})
export class TaskForm {
  @Input() task: any = null;

  @Output() close = new EventEmitter<void>();
  @Output() refresh = new EventEmitter<void>();

  form: any = {
    title: '',
    description: '',
    dueDate: '',
    status: 'TODO',
  };

  constructor(private taskService: Task) {}

  ngOnInit() {
    if (this.task) {
      this.form = { ...this.task };
    }
  }

  save() {
    if (this.task) {
      // UPDATE
      this.taskService.updateTask(this.task.id, this.form).subscribe(() => {
        this.refresh.emit();
        this.close.emit();
      });
    } else {
      // CREATE
      this.taskService.createTask(this.form).subscribe(() => {
        this.refresh.emit();
        this.close.emit();
      });
    }
  }
}
