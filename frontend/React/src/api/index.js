import { req } from './client'

// ── Auth ────────────────────────────────────────────────────────────────────
export const authApi = {
  login:    (body)          => req('POST', '/auth/login',    body),
  register: (body)          => req('POST', '/auth/register', body),
}

// ── Users ───────────────────────────────────────────────────────────────────
export const usersApi = {
  getProfile:     (token)         => req('GET',    '/users',          null,  token),
  update:         (body, token)   => req('PATCH',  '/users',          body,  token),
  changePassword: (body, token)   => req('PATCH',  '/users/password', body,  token),
  delete:         (token)         => req('DELETE', '/users',          null,  token),
}

// ── Subscriptions ────────────────────────────────────────────────────────────
export const subsApi = {
  getAll:          (token)      => req('GET',    '/subscriptions',                      null,  token),
  getById:         (id, token)  => req('GET',    `/subscriptions/${id}`,               null,  token),
  create:          (body, token)=> req('POST',   '/subscriptions',                      body,  token),
  toggleAutoRenew: (id, token)  => req('POST',   `/subscriptions/${id}/toggle-auto-renew`, null, token),
  cancel:          (id, token)  => req('DELETE', `/subscriptions/${id}`,               null,  token),
  getEvents:       (id, token)  => req('GET',    `/subscriptions/${id}/events`,        null,  token),
}

// ── Services ─────────────────────────────────────────────────────────────────
export const servicesApi = {
  getAll:   (token)      => req('GET', '/services',          null, token),
  getById:  (id, token)  => req('GET', `/services/${id}`,    null, token),
  getPlans: (id, token)  => req('GET', `/services/${id}/plans`, null, token),
}

// ── Devices ──────────────────────────────────────────────────────────────────
export const devicesApi = {
  getAll:           (token)              => req('GET',    '/devices',                                    null,   token),
  getById:          (id, token)          => req('GET',    `/devices/${id}`,                              null,   token),
  getBySubscription:(subId, token)       => req('GET',    `/devices/subscriptions/${subId}`,             null,   token),
  create:           (body, token)        => req('POST',   '/devices',                                    body,   token),
  update:           (id, body, token)    => req('PATCH',  `/devices/${id}`,                              body,   token),
  delete:           (id, token)          => req('DELETE', `/devices/${id}`,                              null,   token),
  attach:           (subId, body, token) => req('POST',   `/devices/subscriptions/${subId}/attach`,      body,   token),
  detach:           (subId, devId, token)=> req('DELETE', `/devices/subscriptions/${subId}/detach/${devId}`, null, token),
}

// ── Payments ──────────────────────────────────────────────────────────────────
export const paymentsApi = {
  getAll: (token) => req('GET', '/payments', null, token),
}

// ── Notifications ─────────────────────────────────────────────────────────────
export const notificationsApi = {
  getAll:     (token)      => req('GET',   '/notifications',        null, token),
  markAsRead: (id, token)  => req('PATCH', `/notifications/${id}/read`, null, token),
}

// ── Admin ─────────────────────────────────────────────────────────────────────
export const adminApi = {
  // users
  getAllUsers:   (token)      => req('GET',    '/admin/users',     null, token),
  deleteUser:   (id, token)  => req('DELETE', `/admin/users/${id}`, null, token),

  // subscriptions
  getAllSubs:    (token)      => req('GET', '/admin/subscriptions', null, token),

  // services
  getAllServices:   (token)           => req('GET',    '/admin/services',       null,  token),
  createService:   (body, token)     => req('POST',   '/admin/services',       body,  token),
  updateService:   (id, body, token) => req('PATCH',  `/admin/services/${id}`, body,  token),
  deleteService:   (id, token)       => req('DELETE', `/admin/services/${id}`, null,  token),

  // plans
  getAllPlans:   (token)           => req('GET',    '/admin/plans',       null,  token),
  createPlan:   (body, token)     => req('POST',   '/admin/plans',       body,  token),
  updatePlan:   (id, body, token) => req('PATCH',  `/admin/plans/${id}`, body,  token),
  deletePlan:   (id, token)       => req('DELETE', `/admin/plans/${id}`, null,  token),

  // catalog (write-only — GET вынесен в catalogApi)
  getBillingPeriods:    (token)      => req('GET',    '/admin/catalog/billing-periods',       null, token),
  createBillingPeriod:  (body, token)=> req('POST',   '/admin/catalog/billing-periods',       body, token),
  deleteBillingPeriod:  (id, token)  => req('DELETE', `/admin/catalog/billing-periods/${id}`, null, token),

  createDeviceType: (body, token)=> req('POST',   '/admin/catalog/device-types',       body, token),
  deleteDeviceType: (id, token)  => req('DELETE', `/admin/catalog/device-types/${id}`, null, token),

  createEventType: (body, token)=> req('POST',   '/admin/catalog/event-types',       body, token),
  deleteEventType: (id, token)  => req('DELETE', `/admin/catalog/event-types/${id}`, null, token),
}

// ── Catalog (публичные справочники, доступны всем) ────────────────────────────
export const catalogApi = {
  getDeviceTypes: (token) => req('GET', '/catalog/device-types', null, token),
}
