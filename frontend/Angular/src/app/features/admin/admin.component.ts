import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { User, Subscription, Service, Plan, BillingPeriod } from '../../core/models';
import { forkJoin } from 'rxjs';

type AdminTab = 'users' | 'subs' | 'services' | 'plans';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-content">

      <!-- Tab bar -->
      <div class="admin-tabs">
        @for (t of tabs; track t.key) {
          <button class="btn btn-sm" [class]="activeTab() === t.key ? 'btn-primary' : 'btn-ghost'"
            style="border:none" (click)="switchTab(t.key)">{{ t.label }}</button>
        }
      </div>

      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else {

        <!-- Users -->
        @if (activeTab() === 'users') {
          <div class="card">
            <div class="card-title">All users ({{ users().length }})</div>
            <div class="table-wrap">
              <table>
                <thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Joined</th><th></th></tr></thead>
                <tbody>
                  @for (u of users(); track u.id) {
                    <tr>
                      <td style="font-weight:500">{{ u.firstName }} {{ u.lastName }}</td>
                      <td style="color:var(--muted)">{{ u.email }}</td>
                      <td><span class="badge" [class]="u.role==='admin'?'badge-yellow':'badge-purple'">{{ u.role }}</span></td>
                      <td style="color:var(--muted);font-size:12px">{{ u.createdAt | date:'shortDate' }}</td>
                      <td><button class="btn btn-danger btn-sm" (click)="deleteUser(u.id)">🗑</button></td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        }

        <!-- Subscriptions -->
        @if (activeTab() === 'subs') {
          <div class="card">
            <div class="card-title">All subscriptions ({{ allSubs().length }})</div>
            <div class="table-wrap">
              <table>
                <thead><tr><th>#</th><th>Service</th><th>Plan</th><th>Price</th><th>Auto-renew</th></tr></thead>
                <tbody>
                  @for (s of allSubs(); track s.id) {
                    <tr>
                      <td style="color:var(--muted)">#{{ s.id }}</td>
                      <td style="font-weight:500">{{ s.plan?.service?.name }}</td>
                      <td>{{ s.plan?.name }}</td>
                      <td>{{ s.plan?.price }} {{ s.plan?.currency }}</td>
                      <td><span class="badge" [class]="s.autoRenew?'badge-green':'badge-muted'">{{ s.autoRenew ? 'Yes' : 'No' }}</span></td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        }

        <!-- Services -->
        @if (activeTab() === 'services') {
          <div class="card">
            <div class="flex-between mb-16">
              <div class="card-title" style="margin:0">Services ({{ services().length }})</div>
              <button class="btn btn-primary btn-sm" (click)="openCreateSvc()">＋ Add</button>
            </div>
            <div class="table-wrap">
              <table>
                <thead><tr><th>Name</th><th>Description</th><th>Website</th><th></th></tr></thead>
                <tbody>
                  @for (s of services(); track s.id) {
                    <tr>
                      <td style="font-weight:500">{{ s.name }}</td>
                      <td style="color:var(--muted)">{{ s.description }}</td>
                      <td><a [href]="s.websiteUrl" target="_blank" style="color:var(--accent2);font-size:12px">{{ s.websiteUrl }}</a></td>
                      <td>
                        <div class="flex-gap">
                          <button class="btn btn-ghost btn-sm" (click)="openEditSvc(s)">✏️</button>
                          <button class="btn btn-danger btn-sm" (click)="deleteSvc(s.id)">🗑</button>
                        </div>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        }

        <!-- Plans -->
        @if (activeTab() === 'plans') {
          <div class="card">
            <div class="flex-between mb-16">
              <div class="card-title" style="margin:0">Plans ({{ plans().length }})</div>
              <button class="btn btn-primary btn-sm" (click)="openCreatePlan()">＋ Add</button>
            </div>
            <div class="table-wrap">
              <table>
                <thead><tr><th>Service</th><th>Name</th><th>Price</th><th>Period</th><th>Devices</th><th></th></tr></thead>
                <tbody>
                  @for (p of plans(); track p.id) {
                    <tr>
                      <td style="font-weight:500">{{ p.service?.name }}</td>
                      <td>{{ p.name }}</td>
                      <td>{{ p.price }} {{ p.currency }}</td>
                      <td><span class="chip">{{ p.billingPeriod }}</span></td>
                      <td>{{ p.maxDevices ?? '∞' }}</td>
                      <td>
                        <div class="flex-gap">
                          <button class="btn btn-ghost btn-sm" (click)="openEditPlan(p)">✏️</button>
                          <button class="btn btn-danger btn-sm" (click)="deletePlan(p.id)">🗑</button>
                        </div>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        }

      }
    </div>

    <!-- Service modal -->
    @if (showSvcModal()) {
      <div class="overlay" (click)="closeOverlay($event, 'svc')">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">{{ editSvc() ? 'Edit service' : 'Add service' }}</div>
            <button class="btn btn-ghost btn-sm" (click)="showSvcModal.set(false)">✕</button>
          </div>
          @for (field of svcFields; track field.key) {
            <div class="form-group">
              <label class="form-label">{{ field.label }}</label>
              <input class="form-input" [(ngModel)]="svcForm[field.key]">
            </div>
          }
          <div class="modal-footer">
            <button class="btn btn-ghost" (click)="showSvcModal.set(false)">Cancel</button>
            <button class="btn btn-primary" (click)="saveSvc()">Save</button>
          </div>
        </div>
      </div>
    }

    <!-- Plan modal -->
    @if (showPlanModal()) {
      <div class="overlay" (click)="closeOverlay($event, 'plan')">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">{{ editPlan() ? 'Edit plan' : 'Add plan' }}</div>
            <button class="btn btn-ghost btn-sm" (click)="showPlanModal.set(false)">✕</button>
          </div>
          @if (!editPlan()) {
            <div class="form-group">
              <label class="form-label">Service</label>
              <select class="form-select" [(ngModel)]="planForm.serviceId">
                <option value="">Select service…</option>
                @for (s of services(); track s.id) { <option [value]="s.id">{{ s.name }}</option> }
              </select>
            </div>
          }
          <div class="form-group">
            <label class="form-label">Plan name</label>
            <input class="form-input" [(ngModel)]="planForm.name">
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Price</label>
              <input class="form-input" type="number" step="0.01" [(ngModel)]="planForm.price">
            </div>
            <div class="form-group">
              <label class="form-label">Currency</label>
              <select class="form-select" [(ngModel)]="planForm.currency">
                <option>EUR</option><option>USD</option><option>GBP</option>
              </select>
            </div>
          </div>
          @if (!editPlan()) {
            <div class="form-group">
              <label class="form-label">Billing period</label>
              <select class="form-select" [(ngModel)]="planForm.billingPeriodId">
                <option value="">Select…</option>
                @for (bp of billingPeriods(); track bp.id) { <option [value]="bp.id">{{ bp.name }}</option> }
              </select>
            </div>
          }
          <div class="form-group">
            <label class="form-label">Max devices (blank = unlimited)</label>
            <input class="form-input" type="number" [(ngModel)]="planForm.maxDevices">
          </div>
          <div class="modal-footer">
            <button class="btn btn-ghost" (click)="showPlanModal.set(false)">Cancel</button>
            <button class="btn btn-primary" (click)="savePlan()">Save</button>
          </div>
        </div>
      </div>
    }
  `
})
export class AdminComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  tabs = [
    { key: 'users' as AdminTab,    label: 'Users'         },
    { key: 'subs'  as AdminTab,    label: 'Subscriptions' },
    { key: 'services' as AdminTab, label: 'Services'      },
    { key: 'plans' as AdminTab,    label: 'Plans'         },
  ];

  activeTab      = signal<AdminTab>('users');
  loading        = signal(false);
  users          = signal<User[]>([]);
  allSubs        = signal<Subscription[]>([]);
  services       = signal<Service[]>([]);
  plans          = signal<Plan[]>([]);
  billingPeriods = signal<BillingPeriod[]>([]);

  // Service modal
  showSvcModal = signal(false);
  editSvc      = signal<Service | null>(null);
  svcForm: Record<string, string> = { name: '', description: '', websiteUrl: '', logoUrl: '' };
  svcFields = [
    { key: 'name',        label: 'Name'        },
    { key: 'description', label: 'Description' },
    { key: 'websiteUrl',  label: 'Website URL' },
    { key: 'logoUrl',     label: 'Logo URL'    },
  ];

  // Plan modal
  showPlanModal = signal(false);
  editPlan      = signal<Plan | null>(null);
  planForm: any = { serviceId: '', name: '', price: '', currency: 'EUR', billingPeriodId: '', maxDevices: '' };

  ngOnInit() { this.switchTab('users'); }

  switchTab(t: AdminTab) {
    this.activeTab.set(t);
    this.loading.set(true);

    const load$ = t === 'users'    ? this.api.adminGetUsers() :
                  t === 'subs'     ? this.api.adminGetSubscriptions() :
                  t === 'services' ? forkJoin([this.api.adminGetServices(), this.api.getBillingPeriods()]) :
                                     forkJoin([this.api.adminGetPlans(), this.api.adminGetServices(), this.api.getBillingPeriods()]);

    (load$ as any).subscribe({
      next: (res: any) => {
        if (t === 'users')    this.users.set(res);
        if (t === 'subs')     this.allSubs.set(res);
        if (t === 'services') { this.services.set(res[0]); this.billingPeriods.set(res[1]); }
        if (t === 'plans')    { this.plans.set(res[0]); this.services.set(res[1]); this.billingPeriods.set(res[2]); }
      },
      error: (e: any) => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }

  deleteUser(id: number) {
    if (!confirm('Delete user?')) return;
    this.api.adminDeleteUser(id).subscribe({
      next: () => { this.toast.success('User deleted'); this.switchTab('users'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  // ── Services ──────────────────────────────────────────────────────────────
  openCreateSvc() {
    this.editSvc.set(null);
    this.svcForm = { name: '', description: '', websiteUrl: '', logoUrl: '' };
    this.showSvcModal.set(true);
  }
  openEditSvc(s: Service) {
    this.editSvc.set(s);
    this.svcForm = { name: s.name, description: s.description ?? '', websiteUrl: s.websiteUrl ?? '', logoUrl: s.logoUrl ?? '' };
    this.showSvcModal.set(true);
  }
  saveSvc() {
    const obs = this.editSvc()
      ? this.api.adminUpdateService(this.editSvc()!.id, this.svcForm)
      : this.api.adminCreateService(this.svcForm);
    obs.subscribe({
      next: () => { this.toast.success(this.editSvc() ? 'Service updated' : 'Service created'); this.showSvcModal.set(false); this.switchTab('services'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }
  deleteSvc(id: number) {
    if (!confirm('Delete service?')) return;
    this.api.adminDeleteService(id).subscribe({
      next: () => { this.toast.success('Service deleted'); this.switchTab('services'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  // ── Plans ─────────────────────────────────────────────────────────────────
  openCreatePlan() {
    this.editPlan.set(null);
    this.planForm = { serviceId: '', name: '', price: '', currency: 'EUR', billingPeriodId: '', maxDevices: '' };
    this.showPlanModal.set(true);
  }
  openEditPlan(p: Plan) {
    this.editPlan.set(p);
    this.planForm = { serviceId: p.service?.id ?? '', name: p.name, price: p.price, currency: p.currency, billingPeriodId: '', maxDevices: p.maxDevices ?? '' };
    this.showPlanModal.set(true);
  }
  savePlan() {
    const obs = this.editPlan()
      ? this.api.adminUpdatePlan(this.editPlan()!.id, { name: this.planForm.name, price: +this.planForm.price, currency: this.planForm.currency, maxDevices: this.planForm.maxDevices ? +this.planForm.maxDevices : null })
      : this.api.adminCreatePlan({ serviceId: +this.planForm.serviceId, name: this.planForm.name, price: +this.planForm.price, currency: this.planForm.currency, billingPeriodId: +this.planForm.billingPeriodId, maxDevices: this.planForm.maxDevices ? +this.planForm.maxDevices : null });
    obs.subscribe({
      next: () => { this.toast.success(this.editPlan() ? 'Plan updated' : 'Plan created'); this.showPlanModal.set(false); this.switchTab('plans'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }
  deletePlan(id: number) {
    if (!confirm('Delete plan?')) return;
    this.api.adminDeletePlan(id).subscribe({
      next: () => { this.toast.success('Plan deleted'); this.switchTab('plans'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  closeOverlay(e: MouseEvent, modal: 'svc' | 'plan') {
    if (!(e.target as HTMLElement).classList.contains('overlay')) return;
    if (modal === 'svc')  this.showSvcModal.set(false);
    if (modal === 'plan') this.showPlanModal.set(false);
  }
}
