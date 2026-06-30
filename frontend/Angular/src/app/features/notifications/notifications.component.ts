import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { Notification } from '../../core/models';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-content">
      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else if (notifs().length === 0) {
        <div class="empty"><div class="empty-icon">🔔</div>No notifications</div>
      } @else {

        @if (unread().length > 0) {
          <div class="section-label">Unread · {{ unread().length }}</div>
          @for (n of unread(); track n.id) {
            <div class="notif-item" style="border-color:color-mix(in srgb,var(--accent) 30%,transparent)">
              <div class="notif-dot"></div>
              <div style="flex:1;font-size:13px">{{ n.message }}</div>
              <div class="flex-gap">
                <div class="notif-time">{{ n.createdAt | date:'shortDate' }}</div>
                <button class="btn btn-ghost btn-sm" (click)="markRead(n.id)" title="Mark as read">✓</button>
              </div>
            </div>
          }
          @if (read().length > 0) { <div class="divider"></div> }
        }

        @if (read().length > 0) {
          <div class="section-label">Read</div>
          @for (n of read(); track n.id) {
            <div class="notif-item" style="opacity:.6">
              <div class="notif-dot read"></div>
              <div style="flex:1;font-size:13px">{{ n.message }}</div>
              <div class="notif-time">{{ n.createdAt | date:'shortDate' }}</div>
            </div>
          }
        }

      }
    </div>
  `
})
export class NotificationsComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  loading = signal(true);
  notifs  = signal<Notification[]>([]);
  unread  = computed(() => this.notifs().filter(n => n.isUnread));
  read    = computed(() => this.notifs().filter(n => !n.isUnread));

  ngOnInit() { this.load(); }

  load() {
    this.api.getNotifications().subscribe({
      next: d => this.notifs.set(d),
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }

  markRead(id: number) {
    this.api.markAsRead(id).subscribe({
      next: () => this.load(),
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }
}
