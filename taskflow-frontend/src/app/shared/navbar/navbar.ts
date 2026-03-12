import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { HasRoleDirective } from '../../directives/has-role.directive';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule, HasRoleDirective],
  templateUrl: './navbar.html',
})
export class Navbar {
  userName = 'User';
  role = '';

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.userName = localStorage.getItem('name') || 'User';

    this.role = localStorage.getItem('role') || '';
  }

  logout() {
    this.auth.logout();

    this.router.navigate(['/login']);
  }
}
