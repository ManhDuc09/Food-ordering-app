const BASE_URL = `${import.meta.env.VITE_API_BASE_URL}/api`

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
