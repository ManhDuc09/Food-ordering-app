const BASE = `${import.meta.env.VITE_API_BASE_URL}/api/admin`

const handle = async (res) => {
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText)
    throw new Error(msg || `HTTP ${res.status}`)
  }
  if (res.status === 204) return null
  return res.json()
}

const req = (url, opts = {}) =>
  fetch(url, {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    ...opts
  }).then(handle)

export const adminApi = {
  getUsers: () => req(`${BASE}/users`),
  updateUserRole: (userId, role, branchId) =>
    req(`${BASE}/users/${userId}/role`, { method: 'PATCH', body: JSON.stringify({ role, branchId: branchId || null }) }),

  createProduct: (data) => req(`${BASE}/products`, { method: 'POST', body: JSON.stringify(data) }),
  updateProduct: (productId, data) => req(`${BASE}/products/${productId}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteProduct: (productId) => req(`${BASE}/products/${productId}`, { method: 'DELETE' }),

  createCategory: (name) => req(`${BASE}/categories`, { method: 'POST', body: JSON.stringify({ name }) }),
  deleteCategory: (categoryId) => req(`${BASE}/categories/${categoryId}`, { method: 'DELETE' }),

  createBranch: (data) => req(`${BASE}/branches`, { method: 'POST', body: JSON.stringify(data) }),
  updateBranch: (branchId, data) => req(`${BASE}/branches/${branchId}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteBranch: (branchId) => req(`${BASE}/branches/${branchId}`, { method: 'DELETE' }),
}
