import { createContext, useContext, useState } from 'react'

const AuthCtx = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('sub_token') || null)
  const [user,  setUser]  = useState(() => {
    try { return JSON.parse(localStorage.getItem('sub_user') || 'null') } catch { return null }
  })

  function login(t, u) {
    setToken(t)
    setUser(u)
    localStorage.setItem('sub_token', t)
    localStorage.setItem('sub_user', JSON.stringify(u))
  }

  function logout() {
    setToken(null)
    setUser(null)
    localStorage.removeItem('sub_token')
    localStorage.removeItem('sub_user')
  }

  return (
    <AuthCtx.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthCtx.Provider>
  )
}

export function useAuth() {
  return useContext(AuthCtx)
}
