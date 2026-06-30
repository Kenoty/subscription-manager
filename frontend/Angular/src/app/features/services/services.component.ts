import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { Service, Plan } from '../../core/models';

const EMOJIS: Record<string, string> = {
  'Netflix': '🎬', 'Spotify': '🎵', 'Adobe CC': '🎨',
  'Microsoft 365': '💼', 'YouTube Premium': '▶️',
  'iCloud': '☁️', 'NordVPN': '🔒', 'Notion': '📝',
  'GitHub': '💻', 'Duolingo': '🦉',
};

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-content">
      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else {
        <div class="grid-3">
          @for (s of services(); track s.id) {
            <div class="service-card" (click)="open(s)">
              <div class="service-icon">{{ emoji(s.name) }}</div>
              <div class="service-name">{{ s.name }}</div>
              <div class="service-desc">{{ s.description }}</div>
              @if (s.websiteUrl) {
                <a class="link" [href]="s.websiteUrl" target="_blank" rel="noopener" (click)="$event.stopPropagation()">
                  🔗 Visit site
                </a>
              }
            </div>
          }
        </div>
      }
    </div>

    @if (selected()) {
      <div class="overlay" (click)="closeOverlay($event)">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">{{ selected()!.name }} — Plans</div>
            <button class="btn btn-ghost btn-sm" (click)="selected.set(null)">✕</button>
          </div>
          @if (plans().length === 0) {
            <div class="empty"><div class="empty-icon">📋</div>No plans available</div>
          } @else {
            <div class="stack">
              @for (p of plans(); track p.id) {
                <div class="plan-row">
                  <div class="flex-between">
                    <div class="plan-row-name">{{ p.name }}</div>
                    <div class="plan-row-price">
                      {{ p.price }} {{ p.currency }}
                      <span class="plan-row-sub">/{{ p.billingPeriod }}</span>
                    </div>
                  </div>
                  <div class="plan-row-meta">
                    {{ p.maxDevices ? p.maxDevices + ' devices max' : 'Unlimited devices' }}
                  </div>
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `
})
export class ServicesComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  loading  = signal(true);
  services = signal<Service[]>([]);
  selected = signal<Service | null>(null);
  plans    = signal<Plan[]>([]);

  ngOnInit() {
    this.api.getServices().subscribe({
      next: d => this.services.set(d),
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }

  open(s: Service) {
    this.selected.set(s);
    this.plans.set([]);
    this.api.getServicePlans(s.id).subscribe({
      next: d => this.plans.set(d),
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  closeOverlay(e: MouseEvent) {
    if ((e.target as HTMLElement).classList.contains('overlay')) this.selected.set(null);
  }

  emoji(name: string) { return EMOJIS[name] ?? '📦'; }
}
