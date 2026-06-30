import { Injectable, signal } from '@angular/core';
import { Toast } from '../models';

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts = signal<Toast[]>([]);

  show(message: string, type: 'success' | 'error' = 'success') {
    const id = Date.now();
    this.toasts.update(t => [...t, { id, message, type }]);
    setTimeout(() => this.toasts.update(t => t.filter(x => x.id !== id)), 3500);
  }

  success(message: string) { this.show(message, 'success'); }
  error(message: string)   { this.show(message, 'error');   }
}
