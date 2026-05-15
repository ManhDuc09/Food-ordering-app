<script setup>
import { ref, onMounted } from 'vue'
import ProductCard from './ProductCard.vue'
import { fetchProducts } from '@/api/product.js'

const categories = ref([])

const loadData = async () => {
  try {
    const productsData = await fetchProducts()

    // Create a map for categories based on products' categoryNames
    const categoryMap = {}

    productsData.forEach(product => {
      if (product.categoryNames && product.categoryNames.length > 0) {
        product.categoryNames.forEach(catName => {
          if (!categoryMap[catName]) {
            categoryMap[catName] = {
              id: catName, // Use category name as id since we don't have categoryId
              title: catName,
              products: []
            }
          }
          categoryMap[catName].products.push({
            id: product.productId,
            name: product.name,
            price: parseFloat(product.price),
            description: product.description,
            image: product.imageUrl,
            isAvailable: product.isAvailable
          })
        })
      } else {
        // If no categories, put in a default category
        if (!categoryMap['TẤT CẢ SẢN PHẨM']) {
          categoryMap['TẤT CẢ SẢN PHẨM'] = {
            id: 'default',
            title: 'TẤT CẢ SẢN PHẨM',
            products: []
          }
        }
        categoryMap['TẤT CẢ SẢN PHẨM'].products.push({
          id: product.productId,
          name: product.name,
          price: parseFloat(product.price),
          description: product.description,
          image: product.imageUrl,
          isAvailable: product.isAvailable
        })
      }
    })

    // Convert map to array
    categories.value = Object.values(categoryMap)
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="bg-gray-50 min-h-screen py-10 px-4 md:px-8">
    <div class="max-w-7xl mx-auto space-y-16">
      
      <section v-for="category in categories" :key="category.id">
        <h2 class="text-2xl font-extrabold text-gray-800 mb-8 uppercase tracking-tight">
          {{ category.title }}
        </h2>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <ProductCard 
            v-for="product in category.products" 
            :key="product.id" 
            :product="product"
          />
        </div>
      </section>

    </div>
  </div>
</template>