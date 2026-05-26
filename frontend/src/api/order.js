const BASE_URL = 'http://localhost:8080/api'

export const orderApi = {
  async createOrder(orderData) {
    const token = sessionStorage.getItem('token')

    const headers = {
      'Content-Type': 'application/json'
    }

    if (token) {
      headers.Authorization = `Bearer ${token}`
    }

    const response = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers,
      body: JSON.stringify(orderData)
    })

    if (!response.ok) {
      const body = await response.text()
      throw new Error(body || 'Tạo đơn hàng thất bại')
    }

    return response.json()
  }
}
