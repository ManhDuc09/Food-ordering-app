const BASE = `${import.meta.env.VITE_API_BASE_URL}/api/manager`

const req = async (url, opts = {}) => {
  const res = await fetch(url, {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    ...opts
  })
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  return res.json()
}

export const managerApi = {
  getOrders: () => req(`${BASE}/orders`),
  updateStatus: (orderId, status) =>
    req(`${BASE}/orders/${orderId}/status`, { method: 'PATCH', body: JSON.stringify({ status }) })
}
