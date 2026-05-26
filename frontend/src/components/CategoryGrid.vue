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

        <img :src="category.image" class="h-40 w-full object-cover" />

        <div class="p-4 font-bold">
          {{ category.name }}
        </div>
      </router-link>
    </div>
  </main>
</template>