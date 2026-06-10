const BASE_URL = `${import.meta.env.VITE_API_BASE_URL}/api`

export const authApi = {
  async login(email, password) {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
    if (!response.ok) throw new Error('Email hoặc mật khẩu không đúng!')
    return response.json()
  },

  async register(data) {
    const response = await fetch(`${BASE_URL}/auth/register`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    })
    if (!response.ok) {
      const body = await response.json().catch(() => ({}))
      throw new Error(body.error || 'Đăng ký thất bại!')
    }
    return response.json()
  }
}
