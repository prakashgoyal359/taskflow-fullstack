import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  // 🔐 get token
  const token = localStorage.getItem('token');

  // ❌ do not attach token for auth APIs
  const isAuthRequest = req.url.includes('/api/auth');

  if (token && !isAuthRequest) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req).pipe(
    catchError((error) => {
      // 🔴 Token expired or invalid
      if (error.status === 401) {
        console.warn('JWT expired. Logging out...');

        alert('Session expired. Please login again.');

        localStorage.removeItem('token');
        localStorage.removeItem('name');

        router.navigate(['/login']);
      }

      return throwError(() => error);
    }),
  );
};
