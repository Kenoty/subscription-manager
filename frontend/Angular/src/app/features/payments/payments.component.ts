import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { Payment } from '../../core/models';

@Component({
  selector: 'app-payments',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-content">
      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else {
        @if (payments().length > 0) {
          <div class="stat mb-20" style="max-width:260px">
            <div class="stat-label">Total paid</div>
            <div class="stat-value">{{ total() | number:'1.2-2' }}</div>
            <div class="stat-sub">{{ payments().length }} transaction(s)</div>
          </div>
        }

        <div class="card">
          <div class="card-title">💳 Payment history</div>
          @if (payments().length === 0) {
            <div class="empty"><div class="empty-icon">💳</div>No payments recorded</div>
          } @else {
            <div class="table-wrap">
              <table>
                <thead>
                  <tr><th>#</th><th>Amount</th><th>Currency</th><th>Subscription</th><th>Date</th></tr>
                </thead>
                <tbody>
                  @for (p of payments(); track p.id) {
                    <tr>
                      <td style="color:var(--muted)">#{{ p.id }}</td>
                      <td style="font-weight:600">{{ p.amount }}</td>
                      <td><span class="chip">{{ p.currency }}</span></td>
                      <td style="color:var(--muted)">Sub #{{ p.subscriptionId }}</td>
                      <td style="color:var(--muted)">{{ p.paidAt | date:'short' }}</td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          }
        </div>
      }
    </div>
  `
})
export class PaymentsComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  loading  = signal(true);
  payments = signal<Payment[]>([]);
  total()  { return this.payments().reduce((a, p) => a + +p.amount, 0); }

  ngOnInit() {
    this.api.getPayments().subscribe({
      next: d => this.payments.set(d),
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }
}
