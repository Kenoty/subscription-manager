import { useState } from 'react'
import { authApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'

export default function AuthPage() {
  const { login } = useAuth()
  const { toast } = useToast()
  const [tab, setTab] = useState('login')
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({ email: '', password: '', firstName: '', lastName: '' })

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }))

  async function submit() {
    setLoading(true)
    try {
      let data
      if (tab === 'login') {
        data = await authApi.login({ email: form.email, password: form.password })
      } else {
        data = await authApi.register({
          email: form.email,
          password: form.password,
          firstName: form.firstName,
          lastName: form.lastName,
        })
      }
      login(data.token, data)
      toast('Welcome to SubTrack!')
    } catch (e) {
      toast(e.message, 'error')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-logo">SubTrack</div>

        <div className="auth-tab">
          <button className={`auth-tab-btn ${tab === 'login' ? 'active' : ''}`} onClick={() => setTab('login')}>
            Sign in
          </button>
          <button className={`auth-tab-btn ${tab === 'register' ? 'active' : ''}`} onClick={() => setTab('register')}>
            Create account
          </button>
        </div>

        {tab === 'register' && (
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">First name</label>
              <input className="form-input" value={form.firstName} onChange={set('firstName')} placeholder="Alice" />
            </div>
            <div className="form-group">
              <label className="form-label">Last name</label>
              <input className="form-input" value={form.lastName} onChange={set('lastName')} placeholder="Johnson" />
            </div>
          </div>
        )}

        <div className="form-group">
          <label className="form-label">Email</label>
          <input
            className="form-input"
            type="email"
            value={form.email}
            onChange={set('email')}
            placeholder="you@example.com"
          />
        </div>

        <div className="form-group">
          <label className="form-label">Password</label>
          <input
            className="form-input"
            type="password"
            value={form.password}
            onChange={set('password')}
            placeholder="••••••••"
            onKeyDown={e => e.key === 'Enter' && submit()}
          />
        </div>

        <button
          className="btn btn-primary"
          style={{ width: '100%', justifyContent: 'center' }}
          onClick={submit}
          disabled={loading}
        >
          {loading ? 'Loading…' : tab === 'login' ? 'Sign in' : 'Create account'}
        </button>
      </div>
    </div>
  )
}
