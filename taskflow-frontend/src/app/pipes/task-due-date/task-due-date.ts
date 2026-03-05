import { Pipe, PipeTransform } from '@angular/core';


@Pipe({
  name: 'taskDueDate',
  standalone: true,
})


export class TaskDueDatePipe implements PipeTransform {
  
  transform(task: any): string {
    if (!task) return 'upcoming';

    const today = new Date();
    const due = new Date(task.dueDate);

    // remove time
    today.setHours(0, 0, 0, 0);
    due.setHours(0, 0, 0, 0);

    if (task.status === 'DONE') {
      return 'done';
    }

    if (due < today) {
      return 'overdue';
    }

    if (due.getTime() === today.getTime()) {
      return 'today';
    }

    return 'upcoming';
    
  }
  
}
