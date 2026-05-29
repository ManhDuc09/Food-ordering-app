const API_URL = `${import.meta.env.VITE_API_BASE_URL}/api/categories`;

export const fetchCategories = async () => {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error(`Error: ${response.status} - ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error("Failed to fetch categories:", error);
    return []; 
  }
};