const BASE_URL = `${import.meta.env.VITE_API_BASE_URL}/api`;

export const branchApi = {

  async getAllBranches() {
    const response = await fetch(`${BASE_URL}/branches`);
    if (!response.ok) throw new Error('Không thể tải danh sách cửa hàng!');
    return response.json();
  },

  async getBranchById(id) {
    const response = await fetch(`${BASE_URL}/branches/${id}`);
    if (!response.ok) throw new Error('Cửa hàng không tồn tại!');
    return response.json();
  }
};