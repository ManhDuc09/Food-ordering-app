import { reactive } from 'vue'

export const authState = reactive({
  isLoggedIn: !!sessionStorage.getItem('user'),
  user: JSON.parse(sessionStorage.getItem('user') || 'null'),
  sessionExpired: false
})

export const syncAuth = () => {
  authState.isLoggedIn = !!sessionStorage.getItem('user')
  authState.user = JSON.parse(sessionStorage.getItem('user') || 'null')
}

export const logout = async () => {
  try {
    await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/auth/logout`, {
      method: 'POST',
      credentials: 'include'
    })
  } catch {}
  sessionStorage.removeItem('user')
  sessionStorage.removeItem('expiresAt')
  authState.isLoggedIn = false
  authState.user = null
}

export const checkAndHandleExpiry = () => {
  const expiresAt = sessionStorage.getItem('expiresAt')
  if (expiresAt && Date.now() > Number(expiresAt)) {
    logout()
    authState.sessionExpired = true
    return true
  }
  return false
}
