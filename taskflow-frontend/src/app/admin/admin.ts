import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.service';
import { FormsModule } from '@angular/forms';
import { Navbar } from '../shared/navbar/navbar';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-admin',
  templateUrl: './admin.html',
  imports: [FormsModule, Navbar, CommonModule],
  standalone: true,
})
export class Admin implements OnInit {
  users: any[] = [];
  stats: any = {};

  search = '';

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.getUsers().subscribe((res: any) => {
      this.users = res;

      this.stats.total = res.length;

      this.stats.admin = res.filter((u: any) => u.role == 'ADMIN').length;

      this.stats.manager = res.filter((u: any) => u.role == 'MANAGER').length;

      this.stats.inactive = res.filter((u: any) => !u.active).length;
    });
  }

  changeRole(user: any) {
    this.adminService.updateRole(user.id, user.role).subscribe();
  }

  toggleActive(user: any) {
    this.adminService.toggleUser(user.id).subscribe(() => this.loadUsers());
  }

  deleteUser(id: number) {
    if (!confirm('Delete user?')) return;

    this.adminService.deleteUser(id).subscribe(() => this.loadUsers());
  }
}
