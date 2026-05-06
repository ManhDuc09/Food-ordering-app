<script setup>
import { ref, onMounted } from 'vue'
import Hero from '../components/Hero.vue'
import CategoryGrid from '../components/CategoryGrid.vue'
import { fetchCategories } from '../api/categories' 
const categories = ref([])
const isLoading = ref(true)

onMounted(async () => {
  categories.value = await fetchCategories()
  isLoading.value = false
})
</script>

<template>
  <Hero />

  <div v-if="isLoading" class="text-center p-10">Loading yummy food...</div>
  
  <CategoryGrid v-else :categories="categories" />
</template>