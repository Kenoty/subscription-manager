import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, User } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'sub_token';
  private readonly USER_KEY  = 'sub_user';

  private _token  = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));
  private _user   = signal<Partial<User> | null>(this.loadUser());

  token   = this._token.asReadonly();
  user    = this._user.asReadonly();
  isAdmin = computed(() => this._user()?.role === 'admin');
  isLoggedIn = computed(() => !!this._token());

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, { email, password }).pipe(
      tap(res => this.persist(res))
    );
  }

  register(body: { email: string; password: string; firstName: string; lastName: string }) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, body).pipe(
      tap(res => this.persist(res))
    );
  }

  logout() {
    this._token.set(null);
    this._user.set(null);
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/auth']);
  }

  private persist(res: AuthResponse) {
    this._token.set(res.token);
    const u = { firstName: res.firstName, lastName: res.lastName, email: res.email, role: res.role };
    this._user.set(u);
    localStorage.setItem(this.TOKEN_KEY, res.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(u));
  }

  private loadUser(): Partial<User> | null {
    try { return JSON.parse(localStorage.getItem(this.USER_KEY) || 'null'); }
    catch { return null; }
  }
}
