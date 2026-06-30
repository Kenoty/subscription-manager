import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { Subscription, Payment, Notification } from '../../core/models';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-content">
      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else {
        <div class="grid-4 mb-20">
          <div class="stat">
            <div class="stat-label">Subscriptions</div>
            <div class="stat-value" style="color:var(--accent2)">{{ subs().length }}</div>
          </div>
          <div class="stat">
            <div class="stat-label">Est. monthly</div>
            <div class="stat-value">€{{ monthlyTotal() | number:'1.2-2' }}</div>
          </div>
          <div class="stat">
            <div class="stat-label">Total spent</div>
            <div class="stat-value" style="color:var(--green)">{{ totalSpent() | number:'1.2-2' }}</div>
          </div>
          <div class="stat">
            <div class="stat-label">Unread</div>
            <div class="stat-value" [style.color]="unreadCount() > 0 ? 'var(--yellow)' : 'var(--text)'">
              {{ unreadCount() }}
            </div>
          </div>
        </div>

        <div class="grid-2">
          <div class="card">
            <div class="card-title">📦 Recent subscriptions</div>
            @if (subs().length === 0) {
              <div class="empty"><div class="empty-icon">📦</div>No subscriptions yet</div>
            } @else {
              @for (s of subs().slice(0,4); track s.id) {
                <div style="display:flex;justify-content:space-between;align-items:center;padding:10px 0;border-bottom:1px solid var(--border)">
                  <div>
                    <div style="font-weight:500">{{ s.plan?.service?.name }}</div>
                    <div style="font-size:12px;color:var(--muted)">{{ s.plan?.name }} · {{ s.plan?.billingPeriod }}</div>
                  </div>
                  <div style="text-align:right">
                    <div style="font-weight:600">{{ s.plan?.price }} {{ s.plan?.currency }}</div>
                    <span class="badge" [class]="s.autoRenew ? 'badge-green' : 'badge-muted'">
                      {{ s.autoRenew ? 'Auto-renew' : 'Manual' }}
                    </span>
                  </div>
                </div>
              }
            }
          </div>

          <div class="card">
            <div class="card-title">🔔 Recent notifications</div>
            @if (notifs().length === 0) {
              <div class="empty"><div class="empty-icon">🔔</div>All caught up!</div>
            } @else {
              @for (n of notifs().slice(0,5); track n.id) {
                <div class="notif-item">
                  <div class="notif-dot" [class.read]="!n.isUnread"></div>
                  <div style="flex:1;font-size:13px">{{ n.message }}</div>
                  <div class="notif-time">{{ n.createdAt | date:'shortDate' }}</div>
                </div>
              }
            }
          </div>
        </div>
      }
    </div>
  `
})
export class DashboardComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  loading = signal(true);
  subs    = signal<Subscription[]>([]);
  payments = signal<Payment[]>([]);
  notifs  = signal<Notification[]>([]);

  monthlyTotal() {
    return this.subs().reduce((acc, s) => {
      const p = s.plan?.price ?? 0;
      if (s.plan?.billingPeriod === 'monthly') return acc + +p;
      if (s.plan?.billingPeriod === 'yearly')  return acc + +p / 12;
      return acc;
    }, 0);
  }
  totalSpent()  { return this.payments().reduce((a, p) => a + +p.amount, 0); }
  unreadCount() { return this.notifs().filter(n => n.isUnread).length; }

  ngOnInit() {
    forkJoin([this.api.getSubscriptions(), this.api.getPayments(), this.api.getNotifications()]).subscribe({
      next: ([s, p, n]) => { this.subs.set(s); this.payments.set(p); this.notifs.set(n); },
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }
}
