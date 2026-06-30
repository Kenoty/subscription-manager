import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { User } from '../../core/models';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-content">
      <div style="max-width:520px">

        @if (loading()) {
          <div class="loader"><div class="spinner"></div></div>
        } @else {

          <!-- Profile info -->
          <div class="card mb-16">
            <div class="card-title">👤 Profile</div>
            @if (!editMode()) {
              <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;margin-bottom:16px">
                <div><div class="form-label">First name</div>{{ user()?.firstName }}</div>
                <div><div class="form-label">Last name</div>{{ user()?.lastName }}</div>
                <div style="grid-column:1/-1"><div class="form-label">Email</div>{{ user()?.email }}</div>
                <div style="grid-column:1/-1">
                  <div class="form-label">Role</div>
                  <span class="badge" [class]="user()?.role === 'admin' ? 'badge-yellow' : 'badge-purple'">
                    {{ user()?.role }}
                  </span>
                </div>
              </div>
              <button class="btn btn-ghost" (click)="editMode.set(true)">✏️ Edit profile</button>
            } @else {
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">First name</label>
                  <input class="form-input" [(ngModel)]="editFirst">
                </div>
                <div class="form-group">
                  <label class="form-label">Last name</label>
                  <input class="form-input" [(ngModel)]="editLast">
                </div>
              </div>
              <div class="form-group">
                <label class="form-label">Email</label>
                <input class="form-input" [(ngModel)]="editEmail">
              </div>
              <div class="flex-gap">
                <button class="btn btn-primary" (click)="saveProfile()">Save changes</button>
                <button class="btn btn-ghost" (click)="editMode.set(false)">Cancel</button>
              </div>
            }
          </div>

          <!-- Password -->
          <div class="card mb-16">
            <div class="card-title">🔒 Security</div>
            @if (!pwMode()) {
              <button class="btn btn-ghost" (click)="pwMode.set(true)">Change password</button>
            } @else {
              <div class="form-group">
                <label class="form-label">Current password</label>
                <input class="form-input" type="password" [(ngModel)]="currentPw">
              </div>
              <div class="form-group">
                <label class="form-label">New password</label>
                <input class="form-input" type="password" [(ngModel)]="newPw">
              </div>
              <div class="flex-gap">
                <button class="btn btn-primary" (click)="savePw()">Update password</button>
                <button class="btn btn-ghost" (click)="pwMode.set(false)">Cancel</button>
              </div>
            }
          </div>

          <!-- Danger zone -->
          <div class="card danger-card">
            <div class="card-title danger-title">⚠️ Danger zone</div>
            <div style="font-size:13px;color:var(--muted);margin-bottom:12px">
              Permanently delete your account and all associated data.
            </div>
            <button class="btn btn-danger" (click)="deleteAccount()">🗑 Delete account</button>
          </div>

        }
      </div>
    </div>
  `
})
export class ProfileComponent implements OnInit {
  private api   = inject(ApiService);
  private auth  = inject(AuthService);
  private toast = inject(ToastService);

  loading  = signal(true);
  user     = signal<User | null>(null);
  editMode = signal(false);
  pwMode   = signal(false);

  editFirst = ''; editLast = ''; editEmail = '';
  currentPw = ''; newPw = '';

  ngOnInit() {
    this.api.getProfile().subscribe({
      next: u => {
        this.user.set(u);
        this.editFirst = u.firstName; this.editLast = u.lastName; this.editEmail = u.email;
      },
      error: e => this.toast.error(e.error?.message ?? 'Error'),
      complete: () => this.loading.set(false)
    });
  }

  saveProfile() {
    this.api.updateProfile({ firstName: this.editFirst, lastName: this.editLast, email: this.editEmail }).subscribe({
      next: u => { this.user.set(u); this.editMode.set(false); this.toast.success('Profile updated'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  savePw() {
    this.api.changePassword({ currentPassword: this.currentPw, newPassword: this.newPw }).subscribe({
      next: () => { this.pwMode.set(false); this.currentPw = ''; this.newPw = ''; this.toast.success('Password changed'); },
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }

  deleteAccount() {
    if (!confirm('This will permanently delete your account. Continue?')) return;
    this.api.deleteAccount().subscribe({
      next: () => this.auth.logout(),
      error: e => this.toast.error(e.error?.message ?? 'Error')
    });
  }
}
