import { useState, useEffect } from 'react'
import { useAuth } from './context/AuthContext'
import { notificationsApi } from './api'

import Sidebar   from './components/Sidebar'
import AuthPage  from './pages/AuthPage'
import Dashboard       from './pages/Dashboard'
import Subscriptions   from './pages/Subscriptions'
import ServicesCatalog from './pages/ServicesCatalog'
import Devices         from './pages/Devices'
import Payments        from './pages/Payments'
import Notifications   from './pages/Notifications'
import Profile         from './pages/Profile'
import Admin           from './pages/Admin'
import { Icon, Icons } from './components/ui'

const PAGE_TITLES = {
  dashboard:     'Dashboard',
  subscriptions: 'My Subscriptions',
  services:      'Service Catalog',
  devices:       'My Devices',
  payments:      'Payment History',
  notifications: 'Notifications',
  profile:       'Profile',
  admin:         'Admin Panel',
}

const PAGES = {
  dashboard:     Dashboard,
  subscriptions: Subscriptions,
  services:      ServicesCatalog,
  devices:       Devices,
  payments:      Payments,
  notifications: Notifications,
  profile:       Profile,
  admin:         Admin,
}

export default function App() {
  const { token, user } = useAuth()
  const [page, setPage] = useState('dashboard')
  const [unread, setUnread] = useState(0)

  useEffect(() => {
    if (!token) return

    function fetchUnread() {
      notificationsApi.getAll(token)
        .then(d => setUnread((d || []).filter(n => n.isUnread).length))
        .catch(() => {})
    }

    fetchUnread()
    document.addEventListener('visibilitychange', fetchUnread)
    return () => document.removeEventListener('visibilitychange', fetchUnread)
  }, [token, page])

  if (!token) return <AuthPage />

  const PageComponent = PAGES[page] || Dashboard
  const isAdmin = user?.role === 'admin'

  return (
    <div className="app">
      <Sidebar page={page} setPage={setPage} unread={unread} />

      <div className="main">
        <div className="topbar">
          <div className="topbar-title">{PAGE_TITLES[page]}</div>
          <div className="topbar-actions">
            <button
              className="btn btn-ghost btn-sm"
              style={{ position: 'relative' }}
              onClick={() => setPage('notifications')}
            >
              <Icon d={Icons.notif} />
              {unread > 0 && (
                <span style={{
                  position: 'absolute', top: 2, right: 2,
                  width: 8, height: 8, borderRadius: '50%',
                  background: 'var(--yellow)',
                  border: '2px solid var(--surface)',
                }} />
              )}
            </button>

            <button className="btn btn-ghost btn-sm" onClick={() => setPage('profile')}>
              <span style={{
                width: 26, height: 26, borderRadius: '50%',
                background: 'var(--accent)', color: '#fff',
                display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                fontSize: 11, fontWeight: 600,
              }}>
                {user?.firstName?.[0]}{user?.lastName?.[0]}
              </span>
            </button>
          </div>
        </div>

        <PageComponent />
      </div>
    </div>
  )
}
