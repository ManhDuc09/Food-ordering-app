const BASE_URL = 'http://localhost:8080/api'

export const authApi = {
  async login(email, password) {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })

    if (!response.ok) throw new Error('Email hoặc mật khẩu không đúng!')
    return response.json()
  },

  async register(data) {
    const response = await fetch(`${BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    })

    if (!response.ok) throw new Error('Đăng ký thất bại!')
    return response.json()
  }
}