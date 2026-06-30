import { useState, useEffect, useCallback } from 'react'
import { subsApi, servicesApi, devicesApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Modal, Icon, Icons } from '../components/ui'

export default function Subscriptions() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [subs, setSubs] = useState([])
  const [loading, setLoading] = useState(true)
  const [showCreate, setShowCreate] = useState(false)
  const [showEvents, setShowEvents] = useState(null)
  const [showDevices, setShowDevices] = useState(null) // subscription id
  const [events, setEvents] = useState([])
  const [subDevices, setSubDevices] = useState([])     // devices attached to sub
  const [myDevices, setMyDevices] = useState([])       // all user devices

  // create form
  const [services, setServices] = useState([])
  const [plans, setPlans] = useState([])
  const [selectedService, setSelectedService] = useState('')
  const [planId, setPlanId] = useState('')
  const [autoRenew, setAutoRenew] = useState(true)

  const load = useCallback(async (showLoader = true) => {
    if (showLoader) setLoading(true)
    try {
      const [s, sv] = await Promise.all([subsApi.getAll(token), servicesApi.getAll(token)])
      setSubs(s || [])
      setServices(sv || [])
    } catch (e) {
      toast(e.message, 'error')
    } finally {
      setLoading(false)
    }
  }, [token])

  useEffect(() => { load() }, [load])

  async function loadPlans(serviceId) {
    setSelectedService(serviceId)
    setPlanId('')
    if (!serviceId) { setPlans([]); return }
    try {
      const data = await servicesApi.getPlans(serviceId, token)
      setPlans(data || [])
    } catch (e) { toast(e.message, 'error') }
  }

  async function create() {
    try {
      await subsApi.create({ planId: parseInt(planId), autoRenew }, token)
      toast('Subscription created')
      setShowCreate(false)
      setSelectedService(''); setPlanId(''); setPlans([])
      await load(false)
    } catch (e) { toast(e.message, 'error') }
  }

  async function toggleAutoRenew(id) {
    try {
      await subsApi.toggleAutoRenew(id, token)
      toast('Auto-renew updated')
      await load(false)
    } catch (e) { toast(e.message, 'error') }
  }

  async function cancel(id) {
    if (!confirm('Cancel this subscription?')) return
    try {
      await subsApi.cancel(id, token)
      toast('Subscription cancelled')
      await load(false)
    } catch (e) { toast(e.error?.message || e.message, 'error') }
  }

  async function viewEvents(id) {
    try {
      const data = await subsApi.getEvents(id, token)
      setEvents(data || [])
      setShowEvents(id)
    } catch (e) { toast(e.message, 'error') }
  }

  // ── Devices attach/detach ─────────────────────────────────────────────────
  async function openDevices(subId) {
    try {
      const [attached, all] = await Promise.all([
        devicesApi.getBySubscription(subId, token),
        devicesApi.getAll(token),
      ])
      setSubDevices(attached || [])
      setMyDevices(all || [])
      setShowDevices(subId)
    } catch (e) { toast(e.message, 'error') }
  }

  async function attach(subId, deviceId) {
    try {
      await devicesApi.attach(subId, { deviceId }, token)
      toast('Device attached')
      const data = await devicesApi.getBySubscription(subId, token)
      setSubDevices(data || [])
    } catch (e) { toast(e.error?.message || e.message, 'error') }
  }

  async function detach(subId, deviceId) {
    try {
      await devicesApi.detach(subId, deviceId, token)
      toast('Device detached')
      const data = await devicesApi.getBySubscription(subId, token)
      setSubDevices(data || [])
    } catch (e) { toast(e.error?.message || e.message, 'error') }
  }

  const attachedIds = new Set(subDevices.map(d => d.id))
  const sub = subs.find(s => s.id === showDevices)
  const maxDevices = sub?.plan?.maxDevices ?? Infinity

  if (loading) return <Loader />

  return (
    <div className="content">
      <div className="flex-between mb-16">
        <div style={{ fontSize: 13, color: 'var(--muted)' }}>
          {subs.length} subscription{subs.length !== 1 ? 's' : ''}
        </div>
        <button className="btn btn-primary" onClick={() => setShowCreate(true)}>
          <Icon d={Icons.plus} /> New subscription
        </button>
      </div>

      {subs.length === 0 ? (
        <Empty icon="📦" text="No subscriptions yet."
          action={<button className="btn btn-primary" onClick={() => setShowCreate(true)}>Add subscription</button>}
        />
      ) : (
        <div className="stack">
          {subs.map(s => (
            <div key={s.id} className="sub-card">
              <div style={{ flex: 1 }}>
                <div className="flex-gap" style={{ marginBottom: 4, flexWrap: 'wrap' }}>
                  <div className="sub-name">{s.plan?.service?.name}</div>
                  <span className="badge badge-purple">{s.plan?.name}</span>
                  <span className={`badge ${s.autoRenew ? 'badge-green' : 'badge-muted'}`}>
                    {s.autoRenew ? 'Auto-renew on' : 'Auto-renew off'}
                  </span>
                </div>
                <div className="sub-meta">
                  {s.plan?.maxDevices ? `Up to ${s.plan.maxDevices} devices` : 'Unlimited devices'} · Billed {s.plan?.billingPeriod}
                </div>
                <div className="sub-actions">
                  <button className="btn btn-ghost btn-sm" onClick={() => toggleAutoRenew(s.id)}>
                    <Icon d={Icons.refresh} size={12} /> Toggle auto-renew
                  </button>
                  <button className="btn btn-ghost btn-sm" onClick={() => openDevices(s.id)}>
                    📱 Devices
                  </button>
                  <button className="btn btn-ghost btn-sm" onClick={() => viewEvents(s.id)}>
                    History
                  </button>
                  <button className="btn btn-danger btn-sm" onClick={() => cancel(s.id)}>
                    <Icon d={Icons.x} size={12} /> Cancel
                  </button>
                </div>
              </div>
              <div style={{ textAlign: 'right', flexShrink: 0 }}>
                <div className="sub-price">{s.plan?.price} {s.plan?.currency}</div>
                <div className="sub-period">/{s.plan?.billingPeriod}</div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Create modal */}
      {showCreate && (
        <Modal title="New subscription" onClose={() => setShowCreate(false)} footer={
          <>
            <button className="btn btn-ghost" onClick={() => setShowCreate(false)}>Cancel</button>
            <button className="btn btn-primary" onClick={create} disabled={!planId}>Create</button>
          </>
        }>
          <div className="form-group">
            <label className="form-label">Service</label>
            <select className="form-select" value={selectedService} onChange={e => loadPlans(e.target.value)}>
              <option value="">Select a service…</option>
              {services.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>
          {plans.length > 0 && (
            <div className="form-group">
              <label className="form-label">Plan</label>
              <select className="form-select" value={planId} onChange={e => setPlanId(e.target.value)}>
                <option value="">Select a plan…</option>
                {plans.map(p => (
                  <option key={p.id} value={p.id}>
                    {p.name} — {p.price} {p.currency}/{p.billingPeriod}
                  </option>
                ))}
              </select>
            </div>
          )}
          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: 8, cursor: 'pointer' }}>
              <input type="checkbox" checked={autoRenew} onChange={e => setAutoRenew(e.target.checked)} />
              <span style={{ fontSize: 13 }}>Enable auto-renew</span>
            </label>
          </div>
        </Modal>
      )}

      {/* Events modal */}
      {showEvents !== null && (
        <Modal title="Subscription history" onClose={() => setShowEvents(null)}>
          {events.length === 0 ? <Empty icon="📋" text="No events recorded" /> : (
            <div className="table-wrap">
              <table>
                <thead><tr><th>Event</th><th>Date</th><th>Days</th></tr></thead>
                <tbody>
                  {events.map(e => (
                    <tr key={e.id}>
                      <td><span className="badge badge-purple">{e.eventType}</span></td>
                      <td>{e.eventDate}</td>
                      <td>{e.days ?? '—'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Modal>
      )}

      {/* Devices modal */}
      {showDevices !== null && (
        <Modal title={`Devices — ${sub?.plan?.service?.name}`} onClose={() => setShowDevices(null)}>
          {/* Attached devices */}
          {subDevices.length === 0 ? (
            <Empty icon="📱" text="No devices attached yet" />
          ) : (
            <div style={{ marginBottom: 16 }}>
              <div className="section-label" style={{ marginBottom: 8 }}>Attached</div>
              {subDevices.map(d => (
                <div key={d.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--border)' }}>
                  <div>
                    <div style={{ fontWeight: 500, fontSize: 13 }}>{d.name}</div>
                    <div style={{ fontSize: 11, color: 'var(--muted)' }}>{d.type}</div>
                  </div>
                  <button className="btn btn-danger btn-sm" onClick={() => detach(showDevices, d.id)}>
                    Detach
                  </button>
                </div>
              ))}
            </div>
          )}

          {/* Available to attach */}
          {myDevices.filter(d => !attachedIds.has(d.id)).length > 0 && (
            <>
              <div className="divider" />
              <div className="section-label" style={{ marginBottom: 8 }}>
                Available
                {maxDevices !== Infinity && (
                  <span style={{ color: 'var(--muted)', fontWeight: 400, marginLeft: 8 }}>
                    ({subDevices.length}/{maxDevices} slots used)
                  </span>
                )}
              </div>
              {myDevices.filter(d => !attachedIds.has(d.id)).map(d => (
                <div key={d.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--border)' }}>
                  <div>
                    <div style={{ fontWeight: 500, fontSize: 13 }}>{d.name}</div>
                    <div style={{ fontSize: 11, color: 'var(--muted)' }}>{d.type}</div>
                  </div>
                  <button
                    className="btn btn-primary btn-sm"
                    onClick={() => attach(showDevices, d.id)}
                    disabled={maxDevices !== Infinity && subDevices.length >= maxDevices}
                  >
                    Attach
                  </button>
                </div>
              ))}
            </>
          )}
        </Modal>
      )}
    </div>
  )
}
