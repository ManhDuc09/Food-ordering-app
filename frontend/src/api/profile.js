const BASE_URL = 'http://localhost:8080/api'

const authHeader = () => ({
  'Content-Type': 'application/json',
  Authorization: `Bearer ${sessionStorage.getItem('token')}`
})

export const profileApi = {
  async getProfile() {
    const res = await fetch(`${BASE_URL}/user/profile`, { headers: authHeader() })
    if (!res.ok) throw new Error('Không thể tải thông tin')
    return res.json()
  },

  async updateProfile(data) {
    const res = await fetch(`${BASE_URL}/user/profile`, {
      method: 'PUT',
      headers: authHeader(),
      body: JSON.stringify(data)
    })
    if (!res.ok) throw new Error('Cập nhật thất bại')
    return res.json()
  },

  async getAddresses() {
    const res = await fetch(`${BASE_URL}/addresses`, { headers: authHeader() })
    if (!res.ok) throw new Error('Không thể tải địa chỉ')
    return res.json()
  },

  async addAddress(data) {
    const res = await fetch(`${BASE_URL}/addresses`, {
      method: 'POST',
      headers: authHeader(),
      body: JSON.stringify(data)
    })
    if (!res.ok) throw new Error('Thêm địa chỉ thất bại')
    return res.json()
  },

  async updateAddress(id, data) {
    const res = await fetch(`${BASE_URL}/addresses/${id}`, {
      method: 'PUT',
      headers: authHeader(),
      body: JSON.stringify(data)
    })
    if (!res.ok) throw new Error('Cập nhật địa chỉ thất bại')
    return res.json()
  },

  async deleteAddress(id) {
    const res = await fetch(`${BASE_URL}/addresses/${id}`, {
      method: 'DELETE',
      headers: authHeader()
    })
    if (!res.ok) throw new Error('Xóa địa chỉ thất bại')
  },

  async setDefaultAddress(id) {
    const res = await fetch(`${BASE_URL}/addresses/${id}/default`, {
      method: 'PATCH',
      headers: authHeader()
    })
    if (!res.ok) throw new Error('Không thể đặt địa chỉ mặc định')
    return res.json()
  },

  async getMyOrders() {
    const res = await fetch(`${BASE_URL}/orders/my`, { headers: authHeader() })
    if (!res.ok) throw new Error('Không thể tải đơn hàng')
    return res.json()
  },

  async updatePaymentMethod(orderId, method) {
    const res = await fetch(`${BASE_URL}/orders/${orderId}/payment`, {
      method: 'PATCH',
      headers: authHeader(),
      body: JSON.stringify({ method })
    })
    if (!res.ok) throw new Error('Cập nhật thất bại')
    return res.json()
  },

  async cancelOrder(orderId) {
    const res = await fetch(`${BASE_URL}/orders/${orderId}/cancel`, {
      method: 'PATCH',
      headers: authHeader()
    })
    if (!res.ok) {
      const err = await res.json().catch(() => ({}))
      throw new Error(err.error || 'Hủy đơn thất bại')
    }
    return res.json()
  }
}
