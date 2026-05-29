const API_URL = `${import.meta.env.VITE_API_BASE_URL}/api/products`;

export const fetchProducts = async () => {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error(`Error: ${response.status} - ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error("Failed to fetch products:", error);
    return [];
  }
};

export const fetchProduct = async (id) => {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      throw new Error(`Error: ${response.status} - ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error("Failed to fetch product:", error);
    return null;
  }
};