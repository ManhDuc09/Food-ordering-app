<script setup>
import { ref, onMounted } from 'vue'
import ProductCard from './ProductCard.vue'
import { fetchProducts } from '@/api/product.js'
import { fetchCategories } from '@/api/categories.js'

const categories = ref([])
const isLoading = ref(true)

const loadData = async () => {
  try {
    const [categoryData, productsData] = await Promise.all([
      fetchCategories(),
      fetchProducts()
    ])

    const categoryMap = {}

    categoryData.forEach(category => {
      categoryMap[category.name] = {
        categoryId: category.categoryId,
        name: category.name,
        products: []
      }
    })

    productsData.forEach(product => {
      const productEntry = {
        id: product.productId,
        name: product.name,
        price: parseFloat(product.price || 0),
        description: product.description || 'Món ngon sắp có mặt',
        image: product.imageUrl || '/placeholder-image.png',
        isAvailable: product.isAvailable
      }

      if (product.categoryNames && product.categoryNames.length > 0) {
        product.categoryNames.forEach(catName => {
          if (!categoryMap[catName]) {
            categoryMap[catName] = {
              categoryId: `unknown-${catName}`,
              name: catName,
              products: []
            }
          }
          categoryMap[catName].products.push(productEntry)
        })
      } else {
        const fallbackKey = 'uncategorized'
        if (!categoryMap[fallbackKey]) {
          categoryMap[fallbackKey] = {
            categoryId: fallbackKey,
            name: 'Khác',
            products: []
          }
        }
        categoryMap[fallbackKey].products.push(productEntry)
      }
    })

    categories.value = Object.values(categoryMap)
  } catch (error) {
    console.error('Failed to load categories or products:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="bg-gray-50 min-h-screen py-10 px-4 md:px-8">
    <div class="max-w-7xl mx-auto space-y-16">
      <div v-if="isLoading" class="text-center p-10 text-gray-500">
        Loading products and categories...
      </div>

      <template v-else>
        <section v-for="category in categories" :key="category.categoryId">
          <h2 class="text-2xl font-extrabold text-gray-800 mb-8 uppercase tracking-tight">
            {{ category.name }}
          </h2>

          <div v-if="category.products.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <ProductCard 
              v-for="product in category.products" 
              :key="product.id" 
              :product="product"
            />
          </div>

          <div v-else class="rounded-3xl border border-dashed border-gray-300 bg-white p-10 text-center text-gray-500">
            <p class="text-lg font-semibold text-gray-700 mb-2">
              New products for this category are coming soon.
            </p>
            <p class="text-sm">
              Check back later for awesome items in {{ category.name }}.
            </p>
          </div>
        </section>
      </template>
    </div>
  </div>
</template>