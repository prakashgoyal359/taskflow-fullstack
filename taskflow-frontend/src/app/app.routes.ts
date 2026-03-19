import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Dashboard } from './tasks/dashboard/dashboard';
import { Teams } from './teams/teams';
import { Admin } from './admin/admin';
import { SettingsComponent } from './settings/settings.component';
import { authGuard } from './guards/auth-guard';
import { NotFound } from './shared/navbar/notFound/not-found';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },

  { path: 'register', component: Register },

  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard],
  },

  {
    path: 'teams',
    component: Teams,
    canActivate: [authGuard],
  },

  {
    path: 'settings',
    component: SettingsComponent,
  },

  {
    path: 'admin',
    component: Admin,
    canActivate: [authGuard],
  },

  { path: '**', component: NotFound },
];
