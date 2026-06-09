<script setup>
defineProps({
  categories: Array
})
</script>

<template>
  <main class="container mx-auto px-6 py-12">
    <h2 class="text-2xl font-black uppercase mb-8">
      Danh Mục Món Ăn
    </h2>

    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
      <router-link v-for="category in categories" :key="category.name"
           :to="'/menu#' + category.categoryId"
           class="bg-white rounded shadow hover:shadow-xl block cursor-pointer">

        <img v-if="category.imageUrl" :src="category.imageUrl" class="h-40 w-full object-cover" @error="e => e.target.style.display='none'" />
        <div v-else class="h-40 w-full bg-gray-100 flex items-center justify-center text-gray-300">
          <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
        </div>

        <div class="p-4 font-bold">
          {{ category.name }}
        </div>
      </router-link>
    </div>
  </main>
</template>