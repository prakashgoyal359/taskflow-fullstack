import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Dashboard } from './tasks/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { NotFound } from './shared/navbar/notFound/not-found';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: '**', component: NotFound },
];
