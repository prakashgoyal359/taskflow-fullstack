import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './register.html',
})
export class Register {
  fullName = '';
  email = '';
  password = '';
  confirmPassword = '';

  nameError = '';
  emailError = '';
  passwordError = '';
  confirmError = '';

  passwordColor = 'red';

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  // NAME VALIDATION
  validateName() {
    if (!this.fullName || this.fullName.length < 3) {
      this.nameError = 'Name must be at least 3 characters';
    } else {
      this.nameError = '';
    }
  }

  // EMAIL VALIDATION
  validateEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.emailError = 'Enter valid email address';
    } else {
      this.emailError = '';
    }
  }

  // PASSWORD STRENGTH
  checkStrength() {
    if (this.password.length < 6) {
      this.passwordColor = 'red';
      this.passwordError = 'Password too weak';
    } else if (/[0-9]/.test(this.password)) {
      this.passwordColor = 'orange';
      this.passwordError = 'Add special character for strong password';
    }
    if (/[!@#$%^&*]/.test(this.password) && this.password.length >= 8) {
      this.passwordColor = 'green';
      this.passwordError = '';
    }
  }

  // CONFIRM PASSWORD
  validateConfirm() {
    if (this.password !== this.confirmPassword) {
      this.confirmError = 'Passwords do not match';
    } else {
      this.confirmError = '';
    }
  }

  register() {
    // run validations first
    this.validateName();
    this.validateEmail();
    this.validateConfirm();

    if (this.nameError || this.emailError || this.confirmError || this.passwordError) {
      return;
    }

    // API call
    this.auth
      .register({
        fullName: this.fullName,
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: (res) => {
          console.log('Register success:', res);

          alert('Registered Successfully');

          // ✅ redirect to login
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Register error:', err);

          // ❌ Only show error if real error
          alert('Registration failed');
        },
      });
  }
}
