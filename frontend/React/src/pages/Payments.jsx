import { useState, useEffect } from 'react'
import { paymentsApi } from '../api'
import { useAuth } from '../context/AuthContext'
import { useToast } from '../context/ToastContext'
import { Loader, Empty, Icon, Icons } from '../components/ui'

export default function Payments() {
  const { token } = useAuth()
  const { toast } = useToast()
  const [payments, setPayments] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    paymentsApi.getAll(token)
      .then(d => setPayments(d || []))
      .catch(e => toast(e.message, 'error'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <Loader />

  const total = payments.reduce((a, p) => a + parseFloat(p.amount || 0), 0)

  return (
    <div className="content">
      {payments.length > 0 && (
        <div className="stat" style={{ marginBottom: 20, maxWidth: 260 }}>
          <div className="stat-label">Total paid</div>
          <div className="stat-value">{total.toFixed(2)}</div>
          <div className="stat-sub">{payments.length} transaction{payments.length !== 1 ? 's' : ''}</div>
        </div>
      )}

      <div className="card">
        <div className="card-title"><Icon d={Icons.payment} /> Payment history</div>
        {payments.length === 0 ? (
          <Empty icon="💳" text="No payments recorded" />
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Amount</th>
                  <th>Currency</th>
                  <th>Subscription</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {payments.map(p => (
                  <tr key={p.id}>
                    <td style={{ color: 'var(--muted)' }}>#{p.id}</td>
                    <td style={{ fontWeight: 600 }}>{p.amount}</td>
                    <td><span className="chip">{p.currency}</span></td>
                    <td style={{ color: 'var(--muted)' }}>Sub #{p.subscriptionId}</td>
                    <td style={{ color: 'var(--muted)' }}>{new Date(p.paidAt).toLocaleString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
