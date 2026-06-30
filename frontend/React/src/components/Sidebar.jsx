import { useAuth } from '../context/AuthContext'
import { Icon, Icons } from './ui'

const navItems = [
  { key: 'dashboard',     label: 'Dashboard',     icon: Icons.home    },
  { key: 'subscriptions', label: 'Subscriptions', icon: Icons.sub     },
  { key: 'services',      label: 'Services',      icon: Icons.service },
  { key: 'devices',       label: 'Devices',       icon: Icons.device  },
  { key: 'payments',      label: 'Payments',      icon: Icons.payment },
  { key: 'notifications', label: 'Notifications', icon: Icons.notif   },
]

export default function Sidebar({ page, setPage, unread }) {
  const { user, logout } = useAuth()
  const isAdmin = user?.role === 'admin'

  return (
    <div className="sidebar">
      <div className="sidebar-logo">Sub<span>Track</span></div>

      <div className="nav-section">
        <div className="nav-label">Menu</div>
        {navItems.map(item => (
          <button
            key={item.key}
            className={`nav-item ${page === item.key ? 'active' : ''}`}
            onClick={() => setPage(item.key)}
          >
            <Icon d={item.icon} size={15} />
            {item.key === 'notifications' && unread > 0
              ? `Notifications (${unread})`
              : item.label}
          </button>
        ))}
      </div>

      {isAdmin && (
        <div className="nav-section" style={{ marginTop: 8 }}>
          <div className="nav-label">Admin</div>
          <button
            className={`nav-item ${page === 'admin' ? 'active' : ''}`}
            onClick={() => setPage('admin')}
          >
            <Icon d={Icons.admin} size={15} /> Admin panel
          </button>
        </div>
      )}

      <div className="sidebar-bottom">
        <button
          className={`nav-item ${page === 'profile' ? 'active' : ''}`}
          onClick={() => setPage('profile')}
        >
          <Icon d={Icons.user} size={15} /> Profile
        </button>
        <button className="nav-item" onClick={logout}>
          <Icon d={Icons.logout} size={15} /> Sign out
        </button>
      </div>
    </div>
  )
}
