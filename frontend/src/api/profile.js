const BASE_URL = `${import.meta.env.VITE_API_BASE_URL}/api`

const req = async (url, opts = {}) => {
  const res = await fetch(url, {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    ...opts
  })
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  if (res.status === 204) return null
  return res.json()
}

export const profileApi = {
  getProfile: () => req(`${BASE_URL}/user/profile`),

  updateProfile: (data) => req(`${BASE_URL}/user/profile`, { method: 'PUT', body: JSON.stringify(data) }),

  getAddresses: () => req(`${BASE_URL}/addresses`),

  addAddress: (data) => req(`${BASE_URL}/addresses`, { method: 'POST', body: JSON.stringify(data) }),

  updateAddress: (id, data) => req(`${BASE_URL}/addresses/${id}`, { method: 'PUT', body: JSON.stringify(data) }),

  deleteAddress: (id) => req(`${BASE_URL}/addresses/${id}`, { method: 'DELETE' }),

  setDefaultAddress: (id) => req(`${BASE_URL}/addresses/${id}/default`, { method: 'PATCH' }),

  getMyOrders: () => req(`${BASE_URL}/orders/my`),

  updatePaymentMethod: (orderId, method) =>
    req(`${BASE_URL}/orders/${orderId}/payment`, { method: 'PATCH', body: JSON.stringify({ method }) }),

  cancelOrder: (orderId) => req(`${BASE_URL}/orders/${orderId}/cancel`, { method: 'PATCH' })
}
