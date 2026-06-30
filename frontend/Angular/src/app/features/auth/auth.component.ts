import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="auth-page">
      <div class="auth-card">
        <div class="auth-logo">SubTrack</div>

        <div class="auth-tab">
          <button class="auth-tab-btn" [class.active]="tab()==='login'" (click)="tab.set('login')">Sign in</button>
          <button class="auth-tab-btn" [class.active]="tab()==='register'" (click)="tab.set('register')">Create account</button>
        </div>

        @if (tab() === 'register') {
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">First name</label>
              <input class="form-input" [(ngModel)]="firstName" placeholder="Alice">
            </div>
            <div class="form-group">
              <label class="form-label">Last name</label>
              <input class="form-input" [(ngModel)]="lastName" placeholder="Johnson">
            </div>
          </div>
        }

        <div class="form-group">
          <label class="form-label">Email</label>
          <input class="form-input" type="email" [(ngModel)]="email" placeholder="you@example.com">
        </div>
        <div class="form-group">
          <label class="form-label">Password</label>
          <input class="form-input" type="password" [(ngModel)]="password" placeholder="••••••••" (keydown.enter)="submit()">
        </div>

        <button class="btn btn-primary" style="width:100%;justify-content:center" (click)="submit()" [disabled]="loading()">
          {{ loading() ? 'Loading…' : tab() === 'login' ? 'Sign in' : 'Create account' }}
        </button>
      </div>
    </div>
  `
})
export class AuthComponent {
  private auth   = inject(AuthService);
  private toast  = inject(ToastService);
  private router = inject(Router);

  tab      = signal<'login'|'register'>('login');
  loading  = signal(false);
  email    = '';
  password = '';
  firstName = '';
  lastName  = '';

  submit() {
    this.loading.set(true);
    const obs = this.tab() === 'login'
      ? this.auth.login(this.email, this.password)
      : this.auth.register({ email: this.email, password: this.password, firstName: this.firstName, lastName: this.lastName });

    obs.subscribe({
      next: () => { this.toast.success('Welcome to SubTrack!'); this.router.navigate(['/dashboard']); },
      error: e  => { this.toast.error(e.error?.message ?? 'Error'); this.loading.set(false); },
      complete:  () => this.loading.set(false)
    });
  }
}
