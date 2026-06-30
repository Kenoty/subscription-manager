import { useState, useEffect, useCallback } from 'react'
import { adminApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Modal, Icon, Icons } from '../components/ui'

const TABS = [
  { key: 'users',    label: 'Users'         },
  { key: 'subs',     label: 'Subscriptions' },
  { key: 'services', label: 'Services'      },
  { key: 'plans',    label: 'Plans'         },
]

export default function Admin() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [tab, setTab] = useState('users')
  const [loading, setLoading] = useState(false)

  // data
  const [users,    setUsers]    = useState([])
  const [subs,     setSubs]     = useState([])
  const [services, setServices] = useState([])
  const [plans,    setPlans]    = useState([])
  const [billingPeriods, setBillingPeriods] = useState([])

  // service modal
  const [showSvcModal, setShowSvcModal] = useState(false)
  const [editSvc, setEditSvc] = useState(null)
  const [svcForm, setSvcForm] = useState({ name: '', description: '', websiteUrl: '', logoUrl: '' })

  // plan modal
  const [showPlanModal, setShowPlanModal] = useState(false)
  const [editPlan, setEditPlan] = useState(null)
  const [planForm, setPlanForm] = useState({ serviceId: '', name: '', price: '', currency: 'EUR', billingPeriodId: '', maxDevices: '' })

  const loadTab = useCallback(async (t) => {
    setLoading(true)
    try {
      if (t === 'users') {
        setUsers(await adminApi.getAllUsers(token) || [])
      }
      if (t === 'subs') {
        setSubs(await adminApi.getAllSubs(token) || [])
      }
      if (t === 'services') {
        const [sv, bp] = await Promise.all([adminApi.getAllServices(token), adminApi.getBillingPeriods(token)])
        setServices(sv || [])
        setBillingPeriods(bp || [])
      }
      if (t === 'plans') {
        const [pl, sv, bp] = await Promise.all([adminApi.getAllPlans(token), adminApi.getAllServices(token), adminApi.getBillingPeriods(token)])
        setPlans(pl || [])
        setServices(sv || [])
        setBillingPeriods(bp || [])
      }
    } catch (e) { toast(e.message, 'error') }
    finally { setLoading(false) }
  }, [token])

  useEffect(() => { loadTab(tab) }, [tab])

  // ── Users ──────────────────────────────────────────────────────────────────
  async function deleteUser(id) {
    if (!confirm('Delete user?')) return
    try { await adminApi.deleteUser(id, token); toast('User deleted'); loadTab('users') }
    catch (e) { toast(e.message, 'error') }
  }

  // ── Services ───────────────────────────────────────────────────────────────
  function openCreateSvc() {
    setEditSvc(null)
    setSvcForm({ name: '', description: '', websiteUrl: '', logoUrl: '' })
    setShowSvcModal(true)
  }
  function openEditSvc(s) {
    setEditSvc(s)
    setSvcForm({ name: s.name, description: s.description || '', websiteUrl: s.websiteUrl || '', logoUrl: s.logoUrl || '' })
    setShowSvcModal(true)
  }
  async function saveSvc() {
    try {
      if (editSvc) await adminApi.updateService(editSvc.id, svcForm, token)
      else         await adminApi.createService(svcForm, token)
      toast(editSvc ? 'Service updated' : 'Service created')
      setShowSvcModal(false)
      loadTab('services')
    } catch (e) { toast(e.message, 'error') }
  }
  async function deleteSvc(id) {
    if (!confirm('Delete service?')) return
    try { await adminApi.deleteService(id, token); toast('Service deleted'); loadTab('services') }
    catch (e) { toast(e.message, 'error') }
  }

  // ── Plans ──────────────────────────────────────────────────────────────────
  function openCreatePlan() {
    setEditPlan(null)
    setPlanForm({ serviceId: '', name: '', price: '', currency: 'EUR', billingPeriodId: '', maxDevices: '' })
    setShowPlanModal(true)
  }
  function openEditPlan(p) {
    setEditPlan(p)
    setPlanForm({ serviceId: p.service?.id || '', name: p.name, price: p.price, currency: p.currency, billingPeriodId: '', maxDevices: p.maxDevices || '' })
    setShowPlanModal(true)
  }
  async function savePlan() {
    try {
      if (editPlan) {
        await adminApi.updatePlan(editPlan.id, {
          name: planForm.name,
          price: parseFloat(planForm.price),
          currency: planForm.currency,
          maxDevices: planForm.maxDevices ? parseInt(planForm.maxDevices) : null,
        }, token)
      } else {
        await adminApi.createPlan({
          serviceId: parseInt(planForm.serviceId),
          name: planForm.name,
          price: parseFloat(planForm.price),
          currency: planForm.currency,
          billingPeriodId: parseInt(planForm.billingPeriodId),
          maxDevices: planForm.maxDevices ? parseInt(planForm.maxDevices) : null,
        }, token)
      }
      toast(editPlan ? 'Plan updated' : 'Plan created')
      setShowPlanModal(false)
      loadTab('plans')
    } catch (e) { toast(e.message, 'error') }
  }
  async function deletePlan(id) {
    if (!confirm('Delete plan?')) return
    try { await adminApi.deletePlan(id, token); toast('Plan deleted'); loadTab('plans') }
    catch (e) { toast(e.message, 'error') }
  }

  // ── Render ─────────────────────────────────────────────────────────────────
  return (
    <div className="content">

      {/* Tab bar */}
      <div style={{ display: 'flex', gap: 4, marginBottom: 24, background: 'var(--surface)', padding: 4, borderRadius: 8, border: '1px solid var(--border)', width: 'fit-content' }}>
        {TABS.map(t => (
          <button
            key={t.key}
            className={`btn btn-sm ${tab === t.key ? 'btn-primary' : 'btn-ghost'}`}
            style={{ border: 'none' }}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {loading ? <Loader /> : (
        <>
          {/* Users */}
          {tab === 'users' && (
            <div className="card">
              <div className="card-title">All users ({users.length})</div>
              <div className="table-wrap">
                <table>
                  <thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Joined</th><th></th></tr></thead>
                  <tbody>
                    {users.map(u => (
                      <tr key={u.id}>
                        <td style={{ fontWeight: 500 }}>{u.firstName} {u.lastName}</td>
                        <td style={{ color: 'var(--muted)' }}>{u.email}</td>
                        <td><span className={`badge ${u.role === 'admin' ? 'badge-yellow' : 'badge-purple'}`}>{u.role}</span></td>
                        <td style={{ color: 'var(--muted)', fontSize: 12 }}>{new Date(u.createdAt).toLocaleDateString()}</td>
                        <td>
                          <button className="btn btn-danger btn-sm" onClick={() => deleteUser(u.id)}>
                            <Icon d={Icons.trash} size={12} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Subscriptions */}
          {tab === 'subs' && (
            <div className="card">
              <div className="card-title">All subscriptions ({subs.length})</div>
              <div className="table-wrap">
                <table>
                  <thead><tr><th>#</th><th>Service</th><th>Plan</th><th>Price</th><th>Auto-renew</th></tr></thead>
                  <tbody>
                    {subs.map(s => (
                      <tr key={s.id}>
                        <td style={{ color: 'var(--muted)' }}>#{s.id}</td>
                        <td style={{ fontWeight: 500 }}>{s.plan?.service?.name}</td>
                        <td>{s.plan?.name}</td>
                        <td>{s.plan?.price} {s.plan?.currency}</td>
                        <td><span className={`badge ${s.autoRenew ? 'badge-green' : 'badge-muted'}`}>{s.autoRenew ? 'Yes' : 'No'}</span></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Services */}
          {tab === 'services' && (
            <div className="card">
              <div className="flex-between mb-16">
                <div className="card-title" style={{ margin: 0 }}>Services ({services.length})</div>
                <button className="btn btn-primary btn-sm" onClick={openCreateSvc}><Icon d={Icons.plus} /> Add</button>
              </div>
              <div className="table-wrap">
                <table>
                  <thead><tr><th>Name</th><th>Description</th><th>Website</th><th></th></tr></thead>
                  <tbody>
                    {services.map(s => (
                      <tr key={s.id}>
                        <td style={{ fontWeight: 500 }}>{s.name}</td>
                        <td style={{ color: 'var(--muted)' }}>{s.description}</td>
                        <td>
                          {s.websiteUrl && (
                            <a href={s.websiteUrl} target="_blank" rel="noopener noreferrer"
                              style={{ color: 'var(--accent2)', fontSize: 12 }}>{s.websiteUrl}</a>
                          )}
                        </td>
                        <td>
                          <div className="flex-gap">
                            <button className="btn btn-ghost btn-sm" onClick={() => openEditSvc(s)}><Icon d={Icons.edit} size={12} /></button>
                            <button className="btn btn-danger btn-sm" onClick={() => deleteSvc(s.id)}><Icon d={Icons.trash} size={12} /></button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Plans */}
          {tab === 'plans' && (
            <div className="card">
              <div className="flex-between mb-16">
                <div className="card-title" style={{ margin: 0 }}>Plans ({plans.length})</div>
                <button className="btn btn-primary btn-sm" onClick={openCreatePlan}><Icon d={Icons.plus} /> Add</button>
              </div>
              <div className="table-wrap">
                <table>
                  <thead><tr><th>Service</th><th>Name</th><th>Price</th><th>Period</th><th>Devices</th><th></th></tr></thead>
                  <tbody>
                    {plans.map(p => (
                      <tr key={p.id}>
                        <td style={{ fontWeight: 500 }}>{p.service?.name}</td>
                        <td>{p.name}</td>
                        <td>{p.price} {p.currency}</td>
                        <td><span className="chip">{p.billingPeriod}</span></td>
                        <td>{p.maxDevices ?? '∞'}</td>
                        <td>
                          <div className="flex-gap">
                            <button className="btn btn-ghost btn-sm" onClick={() => openEditPlan(p)}><Icon d={Icons.edit} size={12} /></button>
                            <button className="btn btn-danger btn-sm" onClick={() => deletePlan(p.id)}><Icon d={Icons.trash} size={12} /></button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </>
      )}

      {/* Service modal */}
      {showSvcModal && (
        <Modal
          title={editSvc ? 'Edit service' : 'Add service'}
          onClose={() => setShowSvcModal(false)}
          footer={
            <>
              <button className="btn btn-ghost" onClick={() => setShowSvcModal(false)}>Cancel</button>
              <button className="btn btn-primary" onClick={saveSvc}>Save</button>
            </>
          }
        >
          {['name', 'description', 'websiteUrl', 'logoUrl'].map(k => (
            <div className="form-group" key={k}>
              <label className="form-label">{k.replace(/([A-Z])/g, ' $1').replace(/^./, s => s.toUpperCase())}</label>
              <input className="form-input" value={svcForm[k]} onChange={e => setSvcForm(f => ({ ...f, [k]: e.target.value }))} />
            </div>
          ))}
        </Modal>
      )}

      {/* Plan modal */}
      {showPlanModal && (
        <Modal
          title={editPlan ? 'Edit plan' : 'Add plan'}
          onClose={() => setShowPlanModal(false)}
          footer={
            <>
              <button className="btn btn-ghost" onClick={() => setShowPlanModal(false)}>Cancel</button>
              <button className="btn btn-primary" onClick={savePlan}>Save</button>
            </>
          }
        >
          {!editPlan && (
            <div className="form-group">
              <label className="form-label">Service</label>
              <select className="form-select" value={planForm.serviceId} onChange={e => setPlanForm(f => ({ ...f, serviceId: e.target.value }))}>
                <option value="">Select service…</option>
                {services.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
              </select>
            </div>
          )}
          <div className="form-group">
            <label className="form-label">Plan name</label>
            <input className="form-input" value={planForm.name} onChange={e => setPlanForm(f => ({ ...f, name: e.target.value }))} />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Price</label>
              <input className="form-input" type="number" step="0.01" value={planForm.price}
                onChange={e => setPlanForm(f => ({ ...f, price: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">Currency</label>
              <select className="form-select" value={planForm.currency} onChange={e => setPlanForm(f => ({ ...f, currency: e.target.value }))}>
                <option>EUR</option><option>USD</option><option>GBP</option>
              </select>
            </div>
          </div>
          {!editPlan && (
            <div className="form-group">
              <label className="form-label">Billing period</label>
              <select className="form-select" value={planForm.billingPeriodId} onChange={e => setPlanForm(f => ({ ...f, billingPeriodId: e.target.value }))}>
                <option value="">Select…</option>
                {billingPeriods.map(bp => <option key={bp.id} value={bp.id}>{bp.name}</option>)}
              </select>
            </div>
          )}
          <div className="form-group">
            <label className="form-label">Max devices (blank = unlimited)</label>
            <input className="form-input" type="number" value={planForm.maxDevices}
              onChange={e => setPlanForm(f => ({ ...f, maxDevices: e.target.value }))} />
          </div>
        </Modal>
      )}
    </div>
  )
}
