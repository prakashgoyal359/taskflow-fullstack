import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskDueDate } from './task-due-date';

describe('TaskDueDate', () => {
  let component: TaskDueDate;
  let fixture: ComponentFixture<TaskDueDate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskDueDate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskDueDate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
