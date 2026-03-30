<template>
  <div class="min-h-screen bg-gray-50 font-sans text-gray-900">
    <nav class="sticky top-0 z-50 flex items-center justify-between bg-white px-6 py-4 shadow-sm">
      <div class="flex items-center space-x-8">
        <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" alt="KFC Logo" class="h-12" />
        <div class="hidden space-x-6 font-bold uppercase md:flex text-sm">
          <a href="#" class="hover:text-red-600">Thực Đơn</a>
          <a href="#" class="hover:text-red-600">Khuyến Mãi</a>
          <a href="#" class="hover:text-red-600">Dịch Vụ Tiệc</a>
        </div>
      </div>
      <div class="flex items-center space-x-4">
        <button class="font-bold text-sm">English</button>
        <div class="flex items-center space-x-2 bg-red-600 text-white px-4 py-2 rounded-full cursor-pointer">
          <span class="font-bold">0</span>
        </div>
      </div>
    </nav>

    <section class="relative w-full bg-red-700 overflow-hidden lg:h-[450px]">
      <div class="container mx-auto flex flex-col lg:flex-row items-center justify-between px-6 py-12">
        <div class="z-10 text-white space-y-4 max-w-lg">
          <h2 class="bg-yellow-400 text-black inline-block px-2 py-1 font-black text-2xl skew-x-[-10deg]">
            CHƯA NGỌT XAO XUYẾN
          </h2>
          <h1 class="text-6xl font-black italic tracking-tighter uppercase leading-none">
            Bánh Wrap <br/> <span class="text-yellow-400">Xốt Nhiệt Đới</span>
          </h1>
          <div class="bg-white text-red-600 text-3xl font-black px-4 py-2 rounded-lg inline-block">
            CHỈ 35.000đ
          </div>
        </div>
        <div class="mt-8 lg:mt-0 relative">
          <div class="h-64 w-64 bg-yellow-400 rounded-full blur-3xl absolute opacity-30"></div>
          <img src="https://via.placeholder.com/500x400" alt="Promo Item" class="relative z-10 drop-shadow-2xl" />
        </div>
      </div>
    </section>

    <main class="container mx-auto px-6 py-12">
      <h2 class="text-2xl font-black uppercase border-b-2 border-gray-200 pb-4 mb-8 italic">
        Danh Mục Món Ăn
      </h2>
      
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
        <div v-for="category in categories" :key="category.name" 
             class="group cursor-pointer bg-white overflow-hidden rounded-lg shadow-md hover:shadow-xl transition-shadow">
          <div class="h-48 overflow-hidden bg-gray-200">
             <img :src="category.image" class="w-full h-full object-cover group-hover:scale-105 transition-transform" />
          </div>
          <div class="p-4 flex justify-between items-center">
            <span class="font-bold uppercase">{{ category.name }}</span>
            <span class="text-gray-400">→</span>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const categories = ref([
  { name: 'Ưu Đãi', image: 'https://via.placeholder.com/300' },
  { name: 'Món Mới', image: 'https://via.placeholder.com/300' },
  { name: 'Combo 1 Người', image: 'https://via.placeholder.com/300' },
  { name: 'Combo Nhóm', image: 'https://via.placeholder.com/300' },
  { name: 'Gà Rán - Gà Quay', image: 'https://via.placeholder.com/300' },
  { name: 'Burger - Cơm - Mì Ý', image: 'https://via.placeholder.com/300' },
  { name: 'Thức Ăn Nhẹ', image: 'https://via.placeholder.com/300' },
  { name: 'Tráng Miệng', image: 'https://via.placeholder.com/300' },
])
</script>

<style scoped>
/* Custom italic style for the KFC vibe */
h1, h2 {
  font-family: 'Arial Black', sans-serif;
}
</style>