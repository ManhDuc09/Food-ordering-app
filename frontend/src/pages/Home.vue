<script setup>
import Hero from '../components/Hero.vue'
import CategoryGrid from '../components/CategoryGrid.vue'

const categories = [
  { name: 'Ưu Đãi', image: 'https://via.placeholder.com/300' },
  { name: 'Món Mới', image: 'https://via.placeholder.com/300' },
]
</script>

<template>
  <Hero />
  <CategoryGrid :categories="categories" />
</template>