const API_URL = 'http://localhost:8080/api/categories';

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