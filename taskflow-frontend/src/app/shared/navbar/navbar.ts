import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.html',
})
export class Navbar {
  userName = 'User';

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  ngOnInit() {
    const name = localStorage.getItem('name');
    if (name) this.userName = name;
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
