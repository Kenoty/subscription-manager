import { useEffect, useState } from 'react'
import { subsApi, paymentsApi, notificationsApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Icon, Icons } from '../components/ui'

export default function Dashboard() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [subs, setSubs] = useState([])
  const [payments, setPayments] = useState([])
  const [notifs, setNotifs] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      subsApi.getAll(token),
      paymentsApi.getAll(token),
      notificationsApi.getAll(token),
    ])
      .then(([s, p, n]) => { setSubs(s || []); setPayments(p || []); setNotifs(n || []) })
      .catch(e => toast(e.message, 'error'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <Loader />

  const totalMonthly = subs.reduce((acc, s) => {
    const price = parseFloat(s.plan?.price || 0)
    if (s.plan?.billingPeriod === 'monthly') return acc + price
    if (s.plan?.billingPeriod === 'yearly')  return acc + price / 12
    return acc
  }, 0)

  const unread     = notifs.filter(n => n.isUnread).length
  const totalSpent = payments.reduce((a, p) => a + parseFloat(p.amount || 0), 0)

  return (
    <div className="content">
      <div className="grid-4" style={{ marginBottom: 20 }}>
        <div className="stat">
          <div className="stat-label">Subscriptions</div>
          <div className="stat-value" style={{ color: 'var(--accent2)' }}>{subs.length}</div>
        </div>
        <div className="stat">
          <div className="stat-label">Est. monthly</div>
          <div className="stat-value">€{totalMonthly.toFixed(2)}</div>
        </div>
        <div className="stat">
          <div className="stat-label">Total spent</div>
          <div className="stat-value" style={{ color: 'var(--green)' }}>{totalSpent.toFixed(2)}</div>
        </div>
        <div className="stat">
          <div className="stat-label">Unread</div>
          <div className="stat-value" style={{ color: unread > 0 ? 'var(--yellow)' : 'var(--text)' }}>{unread}</div>
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-title"><Icon d={Icons.sub} /> Recent subscriptions</div>
          {subs.length === 0 ? (
            <Empty icon="📦" text="No subscriptions yet" />
          ) : (
            subs.slice(0, 4).map(s => (
              <div key={s.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 0', borderBottom: '1px solid var(--border)' }}>
                <div>
                  <div style={{ fontWeight: 500 }}>{s.plan?.service?.name}</div>
                  <div style={{ fontSize: 12, color: 'var(--muted)' }}>{s.plan?.name} · {s.plan?.billingPeriod}</div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <div style={{ fontWeight: 600 }}>{s.plan?.price} {s.plan?.currency}</div>
                  <span className={`badge ${s.autoRenew ? 'badge-green' : 'badge-muted'}`}>
                    {s.autoRenew ? 'Auto-renew' : 'Manual'}
                  </span>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="card">
          <div className="card-title"><Icon d={Icons.notif} /> Recent notifications</div>
          {notifs.length === 0 ? (
            <Empty icon="🔔" text="All caught up!" />
          ) : (
            notifs.slice(0, 5).map(n => (
              <div key={n.id} className="notif-item">
                <div className={`notif-dot ${n.isUnread ? '' : 'read'}`} />
                <div style={{ flex: 1, fontSize: 13 }}>{n.message}</div>
                <div className="notif-time">{new Date(n.createdAt).toLocaleDateString()}</div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}
