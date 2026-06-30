import { useState, useEffect } from 'react'
import { servicesApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Modal, Icon, Icons } from '../components/ui'

const SERVICE_EMOJIS = {
  Netflix: '🎬', Spotify: '🎵', 'Adobe CC': '🎨',
  'Microsoft 365': '💼', 'YouTube Premium': '▶️',
  iCloud: '☁️', NordVPN: '🔒', Notion: '📝',
  GitHub: '💻', Duolingo: '🦉',
}

export default function ServicesCatalog() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [services, setServices] = useState([])
  const [loading, setLoading] = useState(true)
  const [selected, setSelected] = useState(null)
  const [plans, setPlans] = useState([])

  useEffect(() => {
    servicesApi.getAll(token)
      .then(d => setServices(d || []))
      .catch(e => toast(e.message, 'error'))
      .finally(() => setLoading(false))
  }, [])

  async function openService(s) {
    setSelected(s)
    try {
      const data = await servicesApi.getPlans(s.id, token)
      setPlans(data || [])
    } catch (e) { toast(e.message, 'error') }
  }

  if (loading) return <Loader />

  return (
    <div className="content">
      <div className="grid-3">
        {services.map(s => (
          <div key={s.id} className="service-card" onClick={() => openService(s)}>
            <div className="service-icon">{SERVICE_EMOJIS[s.name] || '📦'}</div>
            <div className="service-name">{s.name}</div>
            <div className="service-desc">{s.description}</div>
            {s.websiteUrl && (
              <a
                href={s.websiteUrl}
                target="_blank"
                rel="noopener noreferrer"
                onClick={e => e.stopPropagation()}
                style={{ fontSize: 11, color: 'var(--accent2)', marginTop: 8, display: 'inline-flex', alignItems: 'center', gap: 4 }}
              >
                <Icon d={Icons.link} size={11} /> Visit site
              </a>
            )}
          </div>
        ))}
      </div>

      {selected && (
        <Modal title={`${selected.name} — Plans`} onClose={() => setSelected(null)}>
          {plans.length === 0 ? (
            <Empty icon="📋" text="No plans available" />
          ) : (
            <div className="stack">
              {plans.map(p => (
                <div key={p.id} style={{ padding: 14, background: 'var(--bg)', borderRadius: 8, border: '1px solid var(--border)' }}>
                  <div className="flex-between">
                    <div style={{ fontWeight: 600 }}>{p.name}</div>
                    <div style={{ fontFamily: 'var(--font-head)', fontWeight: 700 }}>
                      {p.price} {p.currency}
                      <span style={{ fontSize: 11, color: 'var(--muted)', fontWeight: 400 }}>/{p.billingPeriod}</span>
                    </div>
                  </div>
                  <div style={{ fontSize: 12, color: 'var(--muted)', marginTop: 4 }}>
                    {p.maxDevices ? `${p.maxDevices} devices max` : 'Unlimited devices'}
                  </div>
                </div>
              ))}
            </div>
          )}
        </Modal>
      )}
    </div>
  )
}
