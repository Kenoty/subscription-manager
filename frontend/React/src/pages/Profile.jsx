import { useState, useEffect } from 'react'
import { usersApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Icon, Icons } from '../components/ui'

export default function Profile() {
  const { token, logout } = useAuth()
  const { toast } = useToast()
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const [editMode, setEditMode] = useState(false)
  const [pwMode, setPwMode] = useState(false)
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '' })
  const [pw, setPw] = useState({ currentPassword: '', newPassword: '' })

  useEffect(() => {
    usersApi.getProfile(token)
      .then(d => { setUser(d); setForm({ firstName: d.firstName, lastName: d.lastName, email: d.email }) })
      .catch(e => toast(e.message, 'error'))
      .finally(() => setLoading(false))
  }, [])

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }))

  async function update() {
    try {
      const d = await usersApi.update({ firstName: form.firstName, lastName: form.lastName, email: form.email }, token)
      setUser(d)
      setEditMode(false)
      toast('Profile updated')
    } catch (e) { toast(e.message, 'error') }
  }

  async function changePw() {
    try {
      await usersApi.changePassword(pw, token)
      setPwMode(false)
      setPw({ currentPassword: '', newPassword: '' })
      toast('Password changed')
    } catch (e) { toast(e.message, 'error') }
  }

  async function deleteAccount() {
    if (!confirm('This will permanently delete your account. Continue?')) return
    try {
      await usersApi.delete(token)
      logout()
    } catch (e) { toast(e.message, 'error') }
  }

  if (loading) return <Loader />

  return (
    <div className="content">
      <div style={{ maxWidth: 520 }}>

        {/* Profile info */}
        <div className="card" style={{ marginBottom: 16 }}>
          <div className="card-title"><Icon d={Icons.user} /> Profile</div>

          {!editMode ? (
            <>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 16 }}>
                <div>
                  <div className="form-label">First name</div>
                  <div>{user?.firstName}</div>
                </div>
                <div>
                  <div className="form-label">Last name</div>
                  <div>{user?.lastName}</div>
                </div>
                <div style={{ gridColumn: '1/-1' }}>
                  <div className="form-label">Email</div>
                  <div>{user?.email}</div>
                </div>
                <div style={{ gridColumn: '1/-1' }}>
                  <div className="form-label">Role</div>
                  <span className={`badge ${user?.role === 'admin' ? 'badge-yellow' : 'badge-purple'}`}>
                    {user?.role}
                  </span>
                </div>
              </div>
              <button className="btn btn-ghost" onClick={() => setEditMode(true)}>
                <Icon d={Icons.edit} /> Edit profile
              </button>
            </>
          ) : (
            <>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">First name</label>
                  <input className="form-input" value={form.firstName} onChange={set('firstName')} />
                </div>
                <div className="form-group">
                  <label className="form-label">Last name</label>
                  <input className="form-input" value={form.lastName} onChange={set('lastName')} />
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Email</label>
                <input className="form-input" value={form.email} onChange={set('email')} />
              </div>
              <div className="flex-gap">
                <button className="btn btn-primary" onClick={update}>Save changes</button>
                <button className="btn btn-ghost" onClick={() => setEditMode(false)}>Cancel</button>
              </div>
            </>
          )}
        </div>

        {/* Password */}
        <div className="card" style={{ marginBottom: 16 }}>
          <div className="card-title">Security</div>
          {!pwMode ? (
            <button className="btn btn-ghost" onClick={() => setPwMode(true)}>Change password</button>
          ) : (
            <>
              <div className="form-group">
                <label className="form-label">Current password</label>
                <input className="form-input" type="password" value={pw.currentPassword} onChange={e => setPw(p => ({ ...p, currentPassword: e.target.value }))} />
              </div>
              <div className="form-group">
                <label className="form-label">New password</label>
                <input className="form-input" type="password" value={pw.newPassword} onChange={e => setPw(p => ({ ...p, newPassword: e.target.value }))} />
              </div>
              <div className="flex-gap">
                <button className="btn btn-primary" onClick={changePw}>Update password</button>
                <button className="btn btn-ghost" onClick={() => setPwMode(false)}>Cancel</button>
              </div>
            </>
          )}
        </div>

        {/* Danger zone */}
        <div className="card" style={{ borderColor: 'color-mix(in srgb, var(--red) 30%, transparent)' }}>
          <div className="card-title" style={{ color: 'var(--red)' }}>Danger zone</div>
          <div style={{ fontSize: 13, color: 'var(--muted)', marginBottom: 12 }}>
            Permanently delete your account and all associated data.
          </div>
          <button className="btn btn-danger" onClick={deleteAccount}>
            <Icon d={Icons.trash} /> Delete account
          </button>
        </div>

      </div>
    </div>
  )
}
