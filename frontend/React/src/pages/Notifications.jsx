import { useState, useEffect, useCallback } from 'react'
import { notificationsApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Icon, Icons } from '../components/ui'

export default function Notifications() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [notifs, setNotifs] = useState([])
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    try {
      const data = await notificationsApi.getAll(token)
      setNotifs(data || [])
    } catch (e) { toast(e.message, 'error') }
    finally { setLoading(false) }
  }, [token])

  useEffect(() => { load() }, [load])

  async function markRead(id) {
    try {
      await notificationsApi.markAsRead(id, token)
      load()
    } catch (e) { toast(e.message, 'error') }
  }

  if (loading) return <Loader />

  const unread = notifs.filter(n => n.isUnread)
  const read   = notifs.filter(n => !n.isUnread)

  return (
    <div className="content">
      {notifs.length === 0 && <Empty icon="🔔" text="No notifications" />}

      {unread.length > 0 && (
        <>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--muted)', textTransform: 'uppercase', letterSpacing: 1, marginBottom: 12 }}>
            Unread · {unread.length}
          </div>
          {unread.map(n => (
            <div key={n.id} className="notif-item" style={{ borderColor: 'color-mix(in srgb, var(--accent) 30%, transparent)' }}>
              <div className="notif-dot" />
              <div style={{ flex: 1, fontSize: 13 }}>{n.message}</div>
              <div className="flex-gap">
                <div className="notif-time">{new Date(n.createdAt).toLocaleDateString()}</div>
                <button className="btn btn-ghost btn-sm" onClick={() => markRead(n.id)} title="Mark as read">
                  <Icon d={Icons.check} size={12} />
                </button>
              </div>
            </div>
          ))}
          {read.length > 0 && <div className="divider" />}
        </>
      )}

      {read.length > 0 && (
        <>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--muted)', textTransform: 'uppercase', letterSpacing: 1, marginBottom: 12 }}>
            Read
          </div>
          {read.map(n => (
            <div key={n.id} className="notif-item" style={{ opacity: 0.6 }}>
              <div className="notif-dot read" />
              <div style={{ flex: 1, fontSize: 13 }}>{n.message}</div>
              <div className="notif-time">{new Date(n.createdAt).toLocaleDateString()}</div>
            </div>
          ))}
        </>
      )}
    </div>
  )
}
