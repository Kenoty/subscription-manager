import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {
  User, Service, Plan, Subscription, SubscriptionEvent,
  Payment, Notification, Device, DeviceType, BillingPeriod
} from '../models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private base = environment.apiUrl;
  constructor(private http: HttpClient) {}

  // ── Users ────────────────────────────────────────────────────────────────
  getProfile()                          { return this.http.get<User>(`${this.base}/users`); }
  updateProfile(body: Partial<User>)    { return this.http.patch<User>(`${this.base}/users`, body); }
  changePassword(body: object)          { return this.http.patch<void>(`${this.base}/users/password`, body); }
  deleteAccount()                       { return this.http.delete<void>(`${this.base}/users`); }

  // ── Subscriptions ─────────────────────────────────────────────────────────
  getSubscriptions()                    { return this.http.get<Subscription[]>(`${this.base}/subscriptions`); }
  getSubscription(id: number)           { return this.http.get<Subscription>(`${this.base}/subscriptions/${id}`); }
  createSubscription(body: object)      { return this.http.post<Subscription>(`${this.base}/subscriptions`, body); }
  toggleAutoRenew(id: number)           { return this.http.post<Subscription>(`${this.base}/subscriptions/${id}/toggle-auto-renew`, {}); }
  cancelSubscription(id: number)        { return this.http.delete<void>(`${this.base}/subscriptions/${id}`); }
  getSubscriptionEvents(id: number)     { return this.http.get<SubscriptionEvent[]>(`${this.base}/subscriptions/${id}/events`); }

  // ── Services ──────────────────────────────────────────────────────────────
  getServices()                         { return this.http.get<Service[]>(`${this.base}/services`); }
  getService(id: number)                { return this.http.get<Service>(`${this.base}/services/${id}`); }
  getServicePlans(id: number)           { return this.http.get<Plan[]>(`${this.base}/services/${id}/plans`); }

  // ── Devices ───────────────────────────────────────────────────────────────
  getDevices()                              { return this.http.get<Device[]>(`${this.base}/devices`); }
  getDevicesBySubscription(subId: number)   { return this.http.get<Device[]>(`${this.base}/devices/subscriptions/${subId}`); }
  createDevice(body: object)                { return this.http.post<Device>(`${this.base}/devices`, body); }
  updateDevice(id: string, body: object)    { return this.http.patch<Device>(`${this.base}/devices/${id}`, body); }
  deleteDevice(id: string)                  { return this.http.delete<void>(`${this.base}/devices/${id}`); }
  attachDevice(subId: number, body: object) { return this.http.post<void>(`${this.base}/devices/subscriptions/${subId}/attach`, body); }
  detachDevice(subId: number, devId: string){ return this.http.delete<void>(`${this.base}/devices/subscriptions/${subId}/detach/${devId}`); }

  // ── Payments ──────────────────────────────────────────────────────────────
  getPayments()                         { return this.http.get<Payment[]>(`${this.base}/payments`); }

  // ── Notifications ─────────────────────────────────────────────────────────
  getNotifications()                    { return this.http.get<Notification[]>(`${this.base}/notifications`); }
  markAsRead(id: number)                { return this.http.patch<void>(`${this.base}/notifications/${id}/read`, {}); }

  // ── Catalog (публичный) ───────────────────────────────────────────────────
  getDeviceTypes()                      { return this.http.get<DeviceType[]>(`${this.base}/catalog/device-types`); }

  // ── Admin: Users ──────────────────────────────────────────────────────────
  adminGetUsers()                       { return this.http.get<User[]>(`${this.base}/admin/users`); }
  adminDeleteUser(id: number)           { return this.http.delete<void>(`${this.base}/admin/users/${id}`); }

  // ── Admin: Subscriptions ──────────────────────────────────────────────────
  adminGetSubscriptions()               { return this.http.get<Subscription[]>(`${this.base}/admin/subscriptions`); }

  // ── Admin: Services ───────────────────────────────────────────────────────
  adminGetServices()                    { return this.http.get<Service[]>(`${this.base}/admin/services`); }
  adminCreateService(body: object)      { return this.http.post<Service>(`${this.base}/admin/services`, body); }
  adminUpdateService(id: number, b: object) { return this.http.patch<Service>(`${this.base}/admin/services/${id}`, b); }
  adminDeleteService(id: number)        { return this.http.delete<void>(`${this.base}/admin/services/${id}`); }

  // ── Admin: Plans ──────────────────────────────────────────────────────────
  adminGetPlans()                       { return this.http.get<Plan[]>(`${this.base}/admin/plans`); }
  adminCreatePlan(body: object)         { return this.http.post<Plan>(`${this.base}/admin/plans`, body); }
  adminUpdatePlan(id: number, b: object){ return this.http.patch<Plan>(`${this.base}/admin/plans/${id}`, b); }
  adminDeletePlan(id: number)           { return this.http.delete<void>(`${this.base}/admin/plans/${id}`); }

  // ── Admin: Catalog ────────────────────────────────────────────────────────
  getBillingPeriods()                    { return this.http.get<BillingPeriod[]>(`${this.base}/admin/catalog/billing-periods`); }
  createBillingPeriod(body: object)      { return this.http.post<BillingPeriod>(`${this.base}/admin/catalog/billing-periods`, body); }
  deleteBillingPeriod(id: number)        { return this.http.delete<void>(`${this.base}/admin/catalog/billing-periods/${id}`); }

  createDeviceType(body: object)         { return this.http.post<DeviceType>(`${this.base}/admin/catalog/device-types`, body); }
  deleteDeviceType(id: number)           { return this.http.delete<void>(`${this.base}/admin/catalog/device-types/${id}`); }
}
