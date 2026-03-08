import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const token = localStorage.getItem('token');

  // ❌ No token
  if (!token) {
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url },
    });
    return false;
  }

  try {
    // 🔓 decode JWT payload
    const payload = JSON.parse(atob(token.split('.')[1]));

    const expiry = payload.exp * 1000;

    // ❌ Token expired
    if (Date.now() > expiry) {
      console.warn('JWT expired');

      localStorage.removeItem('token');
      localStorage.removeItem('name');

      router.navigate(['/login'], {
        queryParams: { returnUrl: state.url },
      });

      return false;
    }

    // ✅ Token valid
    return true;
  } catch (err) {
    // ❌ Invalid token
    localStorage.removeItem('token');
    localStorage.removeItem('name');

    router.navigate(['/login']);
    return false;
  }
};
