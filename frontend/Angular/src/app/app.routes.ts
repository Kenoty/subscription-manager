import { Routes } from '@angular/router';
import { authGuard, adminGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'auth', loadComponent: () => import('./features/auth/auth.component').then(m => m.AuthComponent) },
  {
    path: '',
    loadComponent: () => import('./shared/components/shell/shell.component').then(m => m.ShellComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard',     loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'subscriptions', loadComponent: () => import('./features/subscriptions/subscriptions.component').then(m => m.SubscriptionsComponent) },
      { path: 'services',      loadComponent: () => import('./features/services/services.component').then(m => m.ServicesComponent) },
      { path: 'devices',       loadComponent: () => import('./features/devices/devices.component').then(m => m.DevicesComponent) },
      { path: 'payments',      loadComponent: () => import('./features/payments/payments.component').then(m => m.PaymentsComponent) },
      { path: 'notifications', loadComponent: () => import('./features/notifications/notifications.component').then(m => m.NotificationsComponent) },
      { path: 'profile',       loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent) },
      { path: 'admin',         loadComponent: () => import('./features/admin/admin.component').then(m => m.AdminComponent), canActivate: [adminGuard] },
    ]
  },
  { path: '**', redirectTo: '' }
];
