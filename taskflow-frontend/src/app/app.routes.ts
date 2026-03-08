import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Dashboard } from './tasks/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { NotFound } from './shared/navbar/notFound/not-found';

export const routes: Routes = [
  // Default route
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public routes
  { path: 'login', component: Login },
  { path: 'register', component: Register },

  // Protected route
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard],
  },

  // 404 page
  { path: '**', component: NotFound },
];
