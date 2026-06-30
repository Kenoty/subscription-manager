import { createContext, useContext, useState, useEffect } from 'react'

const ToastCtx = createContext(null)

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([])

  useEffect(() => {
    if (toasts.length === 0) return
    const t = setTimeout(() => setToasts(p => p.slice(1)), 3500)
    return () => clearTimeout(t)
  }, [toasts])

  function toast(msg, type = 'success') {
    setToasts(p => [...p, { id: Date.now(), msg, type }])
  }

  return (
    <ToastCtx.Provider value={{ toast }}>
      {children}
      <div className="toasts">
        {toasts.map(t => (
          <div key={t.id} className={`toast toast-${t.type}`}>
            {t.type === 'success' ? '✓' : '✕'} {t.msg}
          </div>
        ))}
      </div>
    </ToastCtx.Provider>
  )
}

export function useToast() {
  return useContext(ToastCtx)
}
