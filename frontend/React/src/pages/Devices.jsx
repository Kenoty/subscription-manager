import { useState, useEffect, useCallback } from 'react'
import { devicesApi, catalogApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Modal, Icon, Icons } from '../components/ui'

const DEVICE_EMOJIS = {
  smartphone: '📱', tablet: '📟', laptop: '💻', desktop: '🖥️',
  smart_tv: '📺', game_console: '🎮', 'e-reader': '📚',
  smartwatch: '⌚', streaming_stick: '🔌',
}

// Справочник из БД — публичный эндпоинт отсутствует, захардкожено
const DEVICE_TYPES = [
  { id: 1, name: 'smartphone' },
  { id: 2, name: 'tablet' },
  { id: 3, name: 'laptop' },
  { id: 4, name: 'desktop' },
  { id: 5, name: 'smart_tv' },
  { id: 6, name: 'game_console' },
  { id: 7, name: 'e-reader' },
  { id: 8, name: 'smartwatch' },
  { id: 9, name: 'streaming_stick' },
]

export default function Devices() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [devices, setDevices] = useState([])
  const [deviceTypes, setDeviceTypes] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [editDevice, setEditDevice] = useState(null)
  const [form, setForm] = useState({ name: '', typeId: '', note: '' })

  const load = useCallback(async (showLoader = true) => {
    if (showLoader) setLoading(true)
    try {
      const [d, dt] = await Promise.all([
        devicesApi.getAll(token),
        catalogApi.getDeviceTypes(token).catch(() => []),
      ])
      setDevices(d || [])
      setDeviceTypes(dt || [])
    } catch (e) { toast(e.message, 'error') }
    finally { setLoading(false) }
  }, [token])

  useEffect(() => { load() }, [load])

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }))

  function openCreate() {
    setEditDevice(null)
    setForm({ name: '', typeId: '', note: '' })
    setShowModal(true)
  }

  function openEdit(d) {
    setEditDevice(d)
    setForm({ name: d.name, typeId: d.typeId || '', note: d.note || '' })
    setShowModal(true)
  }

  async function save() {
    try {
      if (editDevice) {
        await devicesApi.update(editDevice.id, { name: form.name, note: form.note || null }, token)
        toast('Device updated')
      } else {
        await devicesApi.create({ name: form.name, typeId: parseInt(form.typeId), note: form.note || null }, token)
        toast('Device added')
      }
      setShowModal(false)
      await load(false)
    } catch (e) { toast(e.message, 'error') }
  }

  async function remove(id) {
    if (!confirm('Remove this device?')) return
    try {
      await devicesApi.delete(id, token)
      toast('Device removed')
      await load(false)
    } catch (e) { toast(e.message, 'error') }
  }

  if (loading) return <Loader />

  return (
    <div className="content">
      <div className="flex-between mb-16">
        <div style={{ fontSize: 13, color: 'var(--muted)' }}>
          {devices.length} device{devices.length !== 1 ? 's' : ''}
        </div>
        <button className="btn btn-primary" onClick={openCreate}>
          <Icon d={Icons.plus} /> Add device
        </button>
      </div>

      {devices.length === 0 ? (
        <Empty icon="📱" text="No devices added yet" action={
          <button className="btn btn-primary" onClick={openCreate}>Add device</button>
        } />
      ) : (
        <div className="grid-2">
          {devices.map(d => (
            <div key={d.id} className="device-card">
              <div className="device-icon">{DEVICE_EMOJIS[d.type] || '📦'}</div>
              <div style={{ flex: 1 }}>
                <div className="device-name">{d.name}</div>
                <div className="device-type">{d.type}</div>
                {d.note && <div style={{ fontSize: 11, color: 'var(--muted)', marginTop: 2 }}>{d.note}</div>}
              </div>
              <div className="flex-gap">
                <button className="btn btn-ghost btn-sm" onClick={() => openEdit(d)}>
                  <Icon d={Icons.edit} size={12} />
                </button>
                <button className="btn btn-danger btn-sm" onClick={() => remove(d.id)}>
                  <Icon d={Icons.trash} size={12} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <Modal
          title={editDevice ? 'Edit device' : 'Add device'}
          onClose={() => setShowModal(false)}
          footer={
            <>
              <button className="btn btn-ghost" onClick={() => setShowModal(false)}>Cancel</button>
              <button className="btn btn-primary" onClick={save}>{editDevice ? 'Save' : 'Add'}</button>
            </>
          }
        >
          <div className="form-group">
            <label className="form-label">Name</label>
            <input className="form-input" value={form.name} onChange={set('name')} placeholder="My iPhone 15" />
          </div>
          {!editDevice && (
            <div className="form-group">
              <label className="form-label">Type</label>
              <select className="form-select" value={form.typeId} onChange={set('typeId')}>
                <option value="">Select type…</option>
                {deviceTypes.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
              </select>
            </div>
          )}
          <div className="form-group">
            <label className="form-label">Note (optional)</label>
            <input className="form-input" value={form.note} onChange={set('note')} placeholder="Work laptop" />
          </div>
        </Modal>
      )}
    </div>
  )
}
