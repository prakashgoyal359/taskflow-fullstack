import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 🔐 get token from localStorage
  const token = localStorage.getItem('token');

  // ❌ Do NOT attach token for auth APIs
  const isAuthRequest = req.url.includes('/api/auth');

  if (token && !isAuthRequest) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req);
};
