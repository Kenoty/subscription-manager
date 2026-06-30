import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { Subscription, SubscriptionEvent, Service, Plan, Device } from '../../core/models';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-subscriptions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-content">
      <div class="flex-between mb-16">
        <div style="font-size:13px;color:var(--muted)">{{ subs().length }} subscription(s)</div>
        <button class="btn btn-primary" (click)="openCreate()">＋ New subscription</button>
      </div>

      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else if (subs().length === 0) {
        <div class="empty">
          <div class="empty-icon">📦</div>
          No subscriptions yet.
          <div style="margin-top:16px"><button class="btn btn-primary" (click)="openCreate()">Add subscription</button></div>
        </div>
      } @else {
        <div class="stack">
          @for (s of subs(); track s.id) {
            <div class="sub-card">
              <div style="flex:1">
                <div class="flex-gap" style="margin-bottom:4px;flex-wrap:wrap">
                  <div class="sub-name">{{ s.plan?.service?.name }}</div>
                  <span class="badge badge-purple">{{ s.plan?.name }}</span>
                  <span class="badge" [class]="s.autoRenew ? 'badge-green' : 'badge-muted'">
                    {{ s.autoRenew ? 'Auto-renew on' : 'Auto-renew off' }}
                  </span>
                </div>
                <div class="sub-meta">
                  {{ s.plan?.maxDevices ? 'Up to ' + s.plan.maxDevices + ' devices' : 'Unlimited devices' }}
                  · Billed {{ s.plan?.billingPeriod }}
                </div>
                <div class="sub-actions">
                  <button class="btn btn-ghost btn-sm" (click)="toggle(s.id)">⟳ Toggle auto-renew</button>
                  <button class="btn btn-ghost btn-sm" (click)="openDevices(s.id, s)">📱 Devices</button>
                  <button class="btn btn-ghost btn-sm" (click)="viewEvents(s.id)">History</button>
                  <button class="btn btn-danger btn-sm" (click)="cancel(s.id)">✕ Cancel</button>
                </div>
              </div>
              <div style="text-align:right;flex-shrink:0">
                <div class="sub-price">{{ s.plan?.price }} {{ s.plan?.currency }}</div>
                <div class="sub-period">/{{ s.plan?.billingPeriod }}</div>
              </div>
            </div>
          }
        </div>
      }
    </div>

    <!-- Create modal -->
    @if (showCreate()) {
      <div class="overlay" (click)="closeOverlay($event, 'create')">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">New subscription</div>
            <button class="btn btn-ghost btn-sm" (click)="showCreate.set(false)">✕</button>
          </div>
          <div class="form-group">
            <label class="form-label">Service</label>
            <select class="form-select" [(ngModel)]="selectedServiceId" (ngModelChange)="loadPlans($event)">
              <option value="">Select a service…</option>
              @for (s of services(); track s.id) {
                <option [value]="s.id">{{ s.name }}</option>
              }
            </select>
          </div>
          @if (plans().length > 0) {
            <div class="form-group">
              <label class="form-label">Plan</label>
              <select class="form-select" [(ngModel)]="selectedPlanId">
                <option value="">Select a plan…</option>
                @for (p of plans(); track p.id) {
                  <option [value]="p.id">{{ p.name }} — {{ p.price }} {{ p.currency }}/{{ p.billingPeriod }}</option>
                }
              </select>
            </div>
          }
          <div class="form-group">
            <label style="display:flex;align-items:center;gap:8px;cursor:pointer;font-size:13px">
              <input type="checkbox" [(ngModel)]="newAutoRenew"> Enable auto-renew
            </label>
          </div>
          <div class="modal-footer">
            <button class="btn btn-ghost" (click)="showCreate.set(false)">Cancel</button>
            <button class="btn btn-primary" (click)="create()" [disabled]="!selectedPlanId">Create</button>
          </div>
        </div>
      </div>
    }

    <!-- Events modal -->
    @if (showEvents()) {
      <div class="overlay" (click)="closeOverlay($event, 'events')">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">Subscription history</div>
            <button class="btn btn-ghost btn-sm" (click)="showEvents.set(false)">✕</button>
          </div>
          @if (events().length === 0) {
            <div class="empty"><div class="empty-icon">📋</div>No events recorded</div>
          } @else {
            <div class="table-wrap">
              <table>
                <thead><tr><th>Event</th><th>Date</th><th>Days</th></tr></thead>
                <tbody>
                  @for (e of events(); track e.id) {
                    <tr>
                      <td><span class="badge badge-purple">{{ e.eventType }}</span></td>
                      <td>{{ e.eventDate }}</td>
                      <td>{{ e.days ?? '—' }}</td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          }
        </div>
      </div>
    }

    <!-- Devices modal -->
    @if (showDevicesModal()) {
      <div class="overlay" (click)="closeOverlay($event, 'devices')">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">Devices — {{ activeSub()?.plan?.service?.name }}</div>
            <button class="btn btn-ghost btn-sm" (click)="showDevicesModal.set(false)">✕</button>
          </div>

          <!-- Attached -->
          @if (subDevices().length === 0) {
            <div class="empty"><div class="empty-icon">📱</div>No devices attached yet</div>
          } @else {
            <div style="margin-bottom:16px">
              <div class="section-label" style="margin-bottom:8px">Attached</div>
              @for (d of subDevices(); track d.id) {
                <div style="display:flex;align-items:center;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--border)">
                  <div>
                    <div style="font-weight:500;font-size:13px">{{ d.name }}</div>
                    <div style="font-size:11px;color:var(--muted)">{{ d.type }}</div>
                  </div>
                  <button class="btn btn-danger btn-sm" (click)="detach(d.id)">Detach</button>
                </div>
              }
            </div>
          }

          <!-- Available -->
          @if (availableDevices().length > 0) {
            <div class="divider"></div>
            <div class="section-label" style="margin-bottom:8px">
              Available
              @if (activeSub()?.plan?.maxDevices) {
                <span style="color:var(--muted);font-weight:400;margin-left:8px">
                  ({{ subDevices().length }}/{{ activeSub()!.plan.maxDevices }} slots used)
                </span>
              }
            </div>
            @for (d of availableDevices(); track d.id) {
              <div style="display:flex;align-items:center;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--border)">
                <div>
                  <div style="font-weight:500;font-size:13px">{{ d.name }}</div>
                  <div style="font-size:11px;color:var(--muted)">{{ d.type }}</div>
                </div>
                <button
                  class="btn btn-primary btn-sm"
                  (click)="attach(d.id)"
                  [disabled]="!!activeSub()?.plan?.maxDevices && subDevices().length >= activeSub()!.plan.maxDevices!"
                >Attach</button>
              </div>
            }
          }
        </div>
      </div>
    }
  `
})
export class SubscriptionsComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  loading    = signal(true);
  subs       = signal<Subscription[]>([]);
  services   = signal<Service[]>([]);
  plans      = signal<Plan[]>([]);
  events     = signal<SubscriptionEvent[]>([]);
  subDevices = signal<Device[]>([]);
  myDevices  = signal<Device[]>([]);
  activeSub  = signal<Subscription | null>(null);
  activeSubId = signal<number | null>(null);

  showCreate       = signal(false);
  showEvents       = signal(false);
  showDevicesModal = signal(false);

  selectedServiceId = '';
  selectedPlanId    = '';
  newAutoRenew      = true;

  attachedIds = computed(() => new Set(this.subDevices().map(d => d.id)));
  availableDevices = computed(() => this.myDevices().filter(d => !this.attachedIds().has(d.id)));

  ngOnInit() { this.load(); }

  load(showLoader = true) {
    if (showLoader) this.loading.set(true);
    forkJoin([this.api.getSubscriptions(), this.api.getServices()]).subscribe({
      next: ([s, sv]) => { this.subs.set(s); this.services.set(sv); },
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }

  openCreate() {
    this.selectedServiceId = ''; this.selectedPlanId = ''; this.plans.set([]); this.newAutoRenew = true;
    this.showCreate.set(true);
  }

  loadPlans(serviceId: string) {
    if (!serviceId) { this.plans.set([]); return; }
    this.api.getServicePlans(+serviceId).subscribe({ next: d => this.plans.set(d) });
  }

  create() {
    this.api.createSubscription({ planId: +this.selectedPlanId, autoRenew: this.newAutoRenew }).subscribe({
      next: () => { this.toast.success('Subscription created'); this.showCreate.set(false); this.load(false); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  toggle(id: number) {
    this.api.toggleAutoRenew(id).subscribe({
      next: () => { this.toast.success('Auto-renew updated'); this.load(false); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  cancel(id: number) {
    if (!confirm('Cancel this subscription?')) return;
    this.api.cancelSubscription(id).subscribe({
      next: () => { this.toast.success('Subscription cancelled'); this.load(false); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  viewEvents(id: number) {
    this.api.getSubscriptionEvents(id).subscribe({
      next: d => { this.events.set(d); this.showEvents.set(true); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  openDevices(subId: number, sub: Subscription) {
    this.activeSubId.set(subId);
    this.activeSub.set(sub);
    forkJoin([this.api.getDevicesBySubscription(subId), this.api.getDevices()]).subscribe({
      next: ([attached, all]) => { this.subDevices.set(attached); this.myDevices.set(all); this.showDevicesModal.set(true); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  attach(deviceId: string) {
    const subId = this.activeSubId()!;
    this.api.attachDevice(subId, { deviceId }).subscribe({
      next: () => {
        this.toast.success('Device attached');
        this.api.getDevicesBySubscription(subId).subscribe({ next: d => this.subDevices.set(d) });
      },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  detach(deviceId: string) {
    const subId = this.activeSubId()!;
    this.api.detachDevice(subId, deviceId).subscribe({
      next: () => {
        this.toast.success('Device detached');
        this.api.getDevicesBySubscription(subId).subscribe({ next: d => this.subDevices.set(d) });
      },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  closeOverlay(e: MouseEvent, modal: string) {
    if (!(e.target as HTMLElement).classList.contains('overlay')) return;
    if (modal === 'create')  this.showCreate.set(false);
    if (modal === 'events')  this.showEvents.set(false);
    if (modal === 'devices') this.showDevicesModal.set(false);
  }
}
