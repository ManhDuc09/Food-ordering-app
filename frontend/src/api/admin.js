const BASE = `${import.meta.env.VITE_API_BASE_URL}/api/admin`

const headers = () => ({
  'Content-Type': 'application/json',
  Authorization: `Bearer ${sessionStorage.getItem('token')}`
})

const handle = async (res) => {
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText)
    throw new Error(msg || `HTTP ${res.status}`)
  }
  if (res.status === 204) return null
  return res.json()
}

export const adminApi = {
  // Users
  getUsers: () => fetch(`${BASE}/users`, { headers: headers() }).then(handle),
  updateUserRole: (userId, role, branchId) =>
    fetch(`${BASE}/users/${userId}/role`, {
      method: 'PATCH',
      headers: headers(),
      body: JSON.stringify({ role, branchId: branchId || null })
    }).then(handle),

  // Products
  createProduct: (data) =>
    fetch(`${BASE}/products`, { method: 'POST', headers: headers(), body: JSON.stringify(data) }).then(handle),
  updateProduct: (productId, data) =>
    fetch(`${BASE}/products/${productId}`, { method: 'PUT', headers: headers(), body: JSON.stringify(data) }).then(handle),
  deleteProduct: (productId) =>
    fetch(`${BASE}/products/${productId}`, { method: 'DELETE', headers: headers() }).then(handle),

  // Categories
  createCategory: (name) =>
    fetch(`${BASE}/categories`, { method: 'POST', headers: headers(), body: JSON.stringify({ name }) }).then(handle),
  deleteCategory: (categoryId) =>
    fetch(`${BASE}/categories/${categoryId}`, { method: 'DELETE', headers: headers() }).then(handle),

  // Branches
  createBranch: (data) =>
    fetch(`${BASE}/branches`, { method: 'POST', headers: headers(), body: JSON.stringify(data) }).then(handle),
  updateBranch: (branchId, data) =>
    fetch(`${BASE}/branches/${branchId}`, { method: 'PUT', headers: headers(), body: JSON.stringify(data) }).then(handle),
  deleteBranch: (branchId) =>
    fetch(`${BASE}/branches/${branchId}`, { method: 'DELETE', headers: headers() }).then(handle),
}
