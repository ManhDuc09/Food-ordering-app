import { reactive } from 'vue'

export const authState = reactive({
  isLoggedIn: !!localStorage.getItem('user'),
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  sessionExpired: false
})

export const syncAuth = () => {
  authState.isLoggedIn = !!localStorage.getItem('user')
  authState.user = JSON.parse(localStorage.getItem('user') || 'null')
}

export const logout = async () => {
  try {
    await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/auth/logout`, {
      method: 'POST',
      credentials: 'include'
    })
  } catch {}
  localStorage.removeItem('user')
  localStorage.removeItem('expiresAt')
  authState.isLoggedIn = false
  authState.user = null
}

export const checkAndHandleExpiry = () => {
  const expiresAt = localStorage.getItem('expiresAt')
  if (expiresAt && Date.now() > Number(expiresAt)) {
    logout()
    authState.sessionExpired = true
    return true
  }
  return false
}
