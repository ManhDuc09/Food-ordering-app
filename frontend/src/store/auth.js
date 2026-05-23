import { reactive } from 'vue'

export const authState = reactive({
  isLoggedIn: !!localStorage.getItem('token'),
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  sessionExpired: false
})

export const syncAuth = () => {
  authState.isLoggedIn = !!localStorage.getItem('token')
  authState.user = JSON.parse(localStorage.getItem('user') || 'null')
}

export const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
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
  const token = localStorage.getItem('token')
  if (token && isTokenExpired(token)) {
    logout()
    authState.sessionExpired = true
    return true
  }
  return false
}
