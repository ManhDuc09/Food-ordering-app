const BASE_URL = 'http://localhost:8080/api';

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