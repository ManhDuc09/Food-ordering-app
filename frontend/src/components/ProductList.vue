<script setup>
import { ref } from 'vue'
import ProductCard from './ProductCard.vue' // Import component con

const categories = ref([
  {
    id: 1,
    title: 'ƯU ĐÃI',
    products: [
      { id: 101, name: 'Combo 99K', price: 99000, originalPrice: 139000, description: '3 Miếng Gà Rán + 1 Mì Ý Popcorn', image: 'https://placehold.co/400x300?text=Combo+99K' },
      { id: 102, name: 'Combo Cùng Vui', price: 199000, originalPrice: 205000, description: 'Combo 6 Miếng Gà Rán Tặng kèm 3 lon Pepsi', image: 'https://placehold.co/400x300?text=Combo+Vui' },
      { id: 103, name: 'Combo Cùng Tiệc', price: 259000, originalPrice: 315000, description: 'Combo 7 Miếng Gà Rán Tặng 2 Miếng Gà + 3 Pepsi', image: 'https://placehold.co/400x300?text=Combo+Tiec' },
    ]
  },
  {
    id: 2,
    title: 'MÓN MỚI',
    products: [
      { id: 201, name: '1 Bánh Wrap Xốt Nhiệt Đới', price: 35000, isNew: true, description: 'Bánh Wrap Gà Tenders giòn cuộn tortilla nướng mềm mại...', image: 'https://placehold.co/400x300?text=Wrap+1' },
      { id: 204, name: 'Khoai Lắc Phô Mai (L)', price: 32000, isNew: true, description: 'Khoai tây chiên giòn tan, lắc đều cùng phô mai thơm ngậy', image: 'https://placehold.co/400x300?text=Khoai+Lac' },
    ]
  }
])
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