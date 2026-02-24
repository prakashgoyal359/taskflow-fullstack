import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  // 🔐 get token
  const token = localStorage.getItem('token');

  // ✅ if token exists → allow access
  if (token) {
    return true;
  }

  // ❌ if no token → redirect to login
  router.navigate(['/login'], {
    queryParams: { returnUrl: state.url }, // optional redirect back after login
  });

  return false;
};
