<template>
  <nav class="sticky top-0 z-50 flex items-center justify-between bg-white px-6 py-4 shadow-sm">
    <div class="flex items-center space-x-8">
      <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" class="h-12" />
      <div class="hidden space-x-6 font-bold uppercase md:flex text-sm">
        <router-link to="/" class="hover:text-red-600">Trang Chủ</router-link>
        <router-link to="/menu" class="hover:text-red-600">Thực Đơn</router-link>
        <router-link to="/map" class="hover:text-red-600">Tìm Nhà Hàng</router-link>
      </div>
    </div>

    <div class="flex items-center space-x-4">

      <!-- NOT logged in -->
      <template v-if="!isLoggedIn">
        <router-link to="/login">
          <button class="font-bold text-sm hover:text-red-600 transition-colors">
            Đăng nhập
          </button>
        </router-link>
        <router-link to="/register">
          <button class="font-bold text-sm bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700 transition-colors">
            Đăng ký
          </button>
        </router-link>
      </template>

      <!-- logged in -->
      <template v-else>
        <span class="font-bold text-sm">{{ user.email }}</span>
        <button @click="handleLogout" class="font-bold text-sm hover:text-red-600 transition-colors">
          Đăng xuất
        </button>
      </template>

      <div class="bg-red-600 text-white px-4 py-2 rounded-full cursor-pointer hover:bg-red-700 transition-colors">
        0
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLoggedIn = ref(false)
const user = ref(null)

onMounted(() => {
  const token = localStorage.getItem('token')
  const userData = localStorage.getItem('user')
  if (token && userData) {
    isLoggedIn.value = true
    user.value = JSON.parse(userData)
  }
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  isLoggedIn.value = false
  user.value = null
  router.push('/')
}
</script>