import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.html',
})
export class Navbar {
  isOpen = false;

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  toggleMenu() {
    this.isOpen = !this.isOpen;
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
