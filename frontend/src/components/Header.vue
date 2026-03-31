<script setup>
</script>

<template>
  <nav class="sticky top-0 z-50 flex items-center justify-between bg-white px-6 py-4 shadow-sm">
    <div class="flex items-center space-x-8">
      <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" class="h-12" />
      <div class="hidden space-x-6 font-bold uppercase md:flex text-sm">
        <router-link to="/" class="hover:text-red-600">Trang Chủ</router-link>
        <router-link to="/menu" class="hover:text-red-600">Thực Đơn</router-link>
        <a href="#" class="hover:text-red-600">Dịch Vụ Tiệc</a>
      </div>
    </div>

    <div class="flex items-center space-x-4">
      <button class="font-bold text-sm">English</button>
      <div class="bg-red-600 text-white px-4 py-2 rounded-full">
        0
      </div>
    </div>
  </nav>
</template>