export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

export interface Service {
  id: number;
  name: string;
  description: string;
  websiteUrl: string;
  logoUrl: string;
}

export interface BillingPeriod {
  id: number;
  name: string;
}

export interface Plan {
  id: number;
  name: string;
  price: number;
  currency: string;
  billingPeriod: string;
  maxDevices: number | null;
  service?: Service;
}

export interface Subscription {
  id: number;
  autoRenew: boolean;
  cancelledAt: string | null;
  plan: Plan;
}

export interface SubscriptionEvent {
  id: number;
  eventType: string;
  eventDate: string;
  days: number | null;
}

export interface Payment {
  id: number;
  amount: number;
  currency: string;
  paidAt: string;
  subscriptionId: number;
}

export interface Notification {
  id: number;
  message: string;
  isUnread: boolean;
  createdAt: string;
}

export interface DeviceType {
  id: number;
  name: string;
}

export interface Device {
  id: string;
  name: string;
  type: string;
  typeId?: number;
  note: string | null;
}

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error';
}
