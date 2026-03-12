import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './login.html',
})
export class Login {
  email = '';
  password = '';

  emailError = '';
  passwordError = '';

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  // EMAIL VALIDATION
  validateEmail() {
    if (!this.email) {
      this.emailError = 'Email is required';
    } else {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.email)) {
        this.emailError = 'Enter a valid email address';
      } else {
        this.emailError = '';
      }
    }
  }

  // PASSWORD VALIDATION
  validatePassword() {
    if (!this.password) {
      this.passwordError = 'Password is required';
    } else if (this.password.length < 6) {
      this.passwordError = 'Password must be at least 6 characters';
    } else {
      this.passwordError = '';
    }
  }

  // LOGIN FUNCTION
  login() {
    this.validateEmail();
    this.validatePassword();

    if (this.emailError || this.passwordError) return;

    this.auth
      .login({
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: (res: any) => {
          this.auth.saveToken(res.token);

          const role = localStorage.getItem('role');

          if (role === 'ADMIN') {
            this.router.navigate(['/admin']);
          } else if (role === 'MANAGER') {
            this.router.navigate(['/teams']);
          } else {
            this.router.navigate(['/dashboard']);
          }
        },

        error: () => {
          alert('Invalid email or password');
        },
      });
  }
}
