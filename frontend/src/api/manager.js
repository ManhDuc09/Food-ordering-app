const BASE = `${import.meta.env.VITE_API_BASE_URL}/api/manager`

const headers = () => ({
  'Content-Type': 'application/json',
  Authorization: `Bearer ${sessionStorage.getItem('token')}`
})

export const managerApi = {
  getOrders: async () => {
    const res = await fetch(`${BASE}/orders`, { headers: headers() })
    if (!res.ok) throw new Error('Failed to fetch orders')
    return res.json()
  },

  updateStatus: async (orderId, status) => {
    const res = await fetch(`${BASE}/orders/${orderId}/status`, {
      method: 'PATCH',
      headers: headers(),
      body: JSON.stringify({ status })
    })
    if (!res.ok) throw new Error('Failed to update status')
    return res.json()
  }
}
