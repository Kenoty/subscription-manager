import { Component, inject, OnInit, OnDestroy, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';

interface NavItem { label: string; route: string; icon: string; }

const NAV: NavItem[] = [
  { label: 'Dashboard',     route: '/dashboard',     icon: 'home'    },
  { label: 'Subscriptions', route: '/subscriptions', icon: 'layers'  },
  { label: 'Services',      route: '/services',      icon: 'tag'     },
  { label: 'Devices',       route: '/devices',       icon: 'cpu'     },
  { label: 'Payments',      route: '/payments',      icon: 'credit-card' },
  { label: 'Notifications', route: '/notifications', icon: 'bell'    },
];

const TITLES: Record<string, string> = {
  '/dashboard':     'Dashboard',
  '/subscriptions': 'My Subscriptions',
  '/services':      'Service Catalog',
  '/devices':       'My Devices',
  '/payments':      'Payment History',
  '/notifications': 'Notifications',
  '/profile':       'Profile',
  '/admin':         'Admin Panel',
};

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  template: `
    <div class="app-shell">
      <!-- Sidebar -->
      <nav class="sidebar">
        <div class="sidebar-logo">Sub<span>Track</span></div>

        <div class="nav-section">
          <div class="nav-label">Menu</div>
          @for (item of nav; track item.route) {
            <a [routerLink]="item.route" routerLinkActive="active" class="nav-item">
              <span class="nav-icon">{{ getIcon(item.icon) }}</span>
              {{ item.route === '/notifications' && unread() > 0
                  ? 'Notifications (' + unread() + ')'
                  : item.label }}
            </a>
          }
        </div>

        @if (auth.isAdmin()) {
          <div class="nav-section" style="margin-top:8px">
            <div class="nav-label">Admin</div>
            <a routerLink="/admin" routerLinkActive="active" class="nav-item">
              🛡️ Admin panel
            </a>
          </div>
        }

        <div class="sidebar-bottom">
          <a routerLink="/profile" routerLinkActive="active" class="nav-item">
            👤 Profile
          </a>
          <button class="nav-item" (click)="auth.logout()">
            🚪 Sign out
          </button>
        </div>
      </nav>

      <!-- Main -->
      <div class="main-area">
        <header class="topbar">
          <div class="topbar-title">{{ pageTitle() }}</div>
          <div class="topbar-actions">
            <a routerLink="/notifications" class="btn btn-ghost btn-sm pos-relative" style="text-decoration:none">
              🔔
              @if (unread() > 0) { <span class="notif-badge"></span> }
            </a>
            <a routerLink="/profile" class="btn btn-ghost btn-sm" style="text-decoration:none">
              <span class="avatar">{{ initials() }}</span>
            </a>
          </div>
        </header>
        <router-outlet />
      </div>
    </div>
  `
})
export class ShellComponent implements OnInit, OnDestroy {
  auth   = inject(AuthService);
  api    = inject(ApiService);
  router = inject(Router);

  nav    = NAV;
  unread = signal(0);
  private visibilityHandler = () => this.loadUnread();

  pageTitle() {
    const url = this.router.url.split('?')[0];
    return TITLES[url] ?? 'SubTrack';
  }

  initials() {
    const u = this.auth.user();
    return (u?.firstName?.[0] ?? '') + (u?.lastName?.[0] ?? '');
  }

  getIcon(name: string): string {
    const map: Record<string,string> = {
      home: '🏠', layers: '📦', tag: '🏷️', cpu: '📱',
      'credit-card': '💳', bell: '🔔'
    };
    return map[name] ?? '•';
  }

  ngOnInit() {
    this.loadUnread();
    document.addEventListener('visibilitychange', this.visibilityHandler);
    this.router.events.subscribe(() => this.loadUnread());
  }

  ngOnDestroy() {
    document.removeEventListener('visibilitychange', this.visibilityHandler);
  }

  loadUnread() {
    this.api.getNotifications().subscribe({
      next: n => this.unread.set(n.filter(x => x.isUnread).length),
      error: () => {}
    });
  }
}
