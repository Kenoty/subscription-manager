import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';
import { Device, DeviceType } from '../../core/models';
import { forkJoin } from 'rxjs';

const DEVICE_EMOJIS: Record<string, string> = {
  smartphone: '📱', tablet: '📟', laptop: '💻', desktop: '🖥️',
  smart_tv: '📺', game_console: '🎮', 'e-reader': '📚',
  smartwatch: '⌚', streaming_stick: '🔌',
};

@Component({
  selector: 'app-devices',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-content">
      <div class="flex-between mb-16">
        <div style="font-size:13px;color:var(--muted)">{{ devices().length }} device(s)</div>
        <button class="btn btn-primary" (click)="openCreate()">＋ Add device</button>
      </div>

      @if (loading()) {
        <div class="loader"><div class="spinner"></div></div>
      } @else if (devices().length === 0) {
        <div class="empty">
          <div class="empty-icon">📱</div>
          No devices yet.
          <div style="margin-top:16px"><button class="btn btn-primary" (click)="openCreate()">Add device</button></div>
        </div>
      } @else {
        <div class="grid-2">
          @for (d of devices(); track d.id) {
            <div class="device-card">
              <div class="device-icon">{{ emoji(d.type) }}</div>
              <div style="flex:1">
                <div class="device-name">{{ d.name }}</div>
                <div class="device-type">{{ d.type }}</div>
                @if (d.note) { <div style="font-size:11px;color:var(--muted);margin-top:2px">{{ d.note }}</div> }
              </div>
              <div class="flex-gap">
                <button class="btn btn-ghost btn-sm" (click)="openEdit(d)">✏️</button>
                <button class="btn btn-danger btn-sm" (click)="remove(d.id)">🗑</button>
              </div>
            </div>
          }
        </div>
      }
    </div>

    @if (showModal()) {
      <div class="overlay" (click)="closeOverlay($event)">
        <div class="modal">
          <div class="modal-header">
            <div class="modal-title">{{ editDevice() ? 'Edit device' : 'Add device' }}</div>
            <button class="btn btn-ghost btn-sm" (click)="showModal.set(false)">✕</button>
          </div>
          <div class="form-group">
            <label class="form-label">Name</label>
            <input class="form-input" [(ngModel)]="formName" placeholder="My iPhone 15">
          </div>
          @if (!editDevice()) {
            <div class="form-group">
              <label class="form-label">Type</label>
              <select class="form-select" [(ngModel)]="formTypeId">
                <option value="">Select type…</option>
                @for (t of deviceTypes(); track t.id) {
                  <option [value]="t.id">{{ t.name }}</option>
                }
              </select>
            </div>
          }
          <div class="form-group">
            <label class="form-label">Note (optional)</label>
            <input class="form-input" [(ngModel)]="formNote" placeholder="Work laptop">
          </div>
          <div class="modal-footer">
            <button class="btn btn-ghost" (click)="showModal.set(false)">Cancel</button>
            <button class="btn btn-primary" (click)="save()">{{ editDevice() ? 'Save' : 'Add' }}</button>
          </div>
        </div>
      </div>
    }
  `
})
export class DevicesComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  loading     = signal(true);
  devices     = signal<Device[]>([]);
  deviceTypes = signal<DeviceType[]>([]);
  showModal   = signal(false);
  editDevice  = signal<Device | null>(null);

  formName   = '';
  formTypeId = '';
  formNote   = '';

  ngOnInit() { this.load(); }

  load(showLoader = true) {
    if (showLoader) this.loading.set(true);
    forkJoin([
      this.api.getDevices(),
      this.api.getDeviceTypes()
    ]).subscribe({
      next: ([d, dt]) => { this.devices.set(d); this.deviceTypes.set(dt); },
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }

  openCreate() {
    this.editDevice.set(null);
    this.formName = ''; this.formTypeId = ''; this.formNote = '';
    this.showModal.set(true);
  }

  openEdit(d: Device) {
    this.editDevice.set(d);
    this.formName = d.name; this.formNote = d.note ?? '';
    this.showModal.set(true);
  }

  closeOverlay(e: MouseEvent) {
    if ((e.target as HTMLElement).classList.contains('overlay')) this.showModal.set(false);
  }

  save() {
    const obs = this.editDevice()
      ? this.api.updateDevice(this.editDevice()!.id, { name: this.formName, note: this.formNote || null })
      : this.api.createDevice({ name: this.formName, typeId: +this.formTypeId, note: this.formNote || null });

    obs.subscribe({
      next: () => {
        this.toast.success(this.editDevice() ? 'Device updated' : 'Device added');
        this.showModal.set(false);
        this.load(false);
      },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  remove(id: string) {
    if (!confirm('Remove this device?')) return;
    this.api.deleteDevice(id).subscribe({
      next: () => { this.toast.success('Device removed'); this.load(false); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  emoji(type: string) { return DEVICE_EMOJIS[type] ?? '📦'; }
}
