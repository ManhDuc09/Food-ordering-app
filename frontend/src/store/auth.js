import { reactive } from 'vue'

export const authState = reactive({
  isLoggedIn: !!sessionStorage.getItem('token'),
  user: JSON.parse(sessionStorage.getItem('user') || 'null'),
  sessionExpired: false
})

export const syncAuth = () => {
  authState.isLoggedIn = !!sessionStorage.getItem('token')
  authState.user = JSON.parse(sessionStorage.getItem('user') || 'null')
}

export const logout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  authState.isLoggedIn = false
  authState.user = null
}

function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')))
    return payload.exp * 1000 < Date.now()
  } catch {
    return true
  }
}

export const checkAndHandleExpiry = () => {
  const token = sessionStorage.getItem('token')
  if (token && isTokenExpired(token)) {
    logout()
    authState.sessionExpired = true
    return true
  }
  return false
}
