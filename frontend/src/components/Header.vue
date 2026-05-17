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
      <template v-if="!authState.isLoggedIn">
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

      <template v-else>
        <span class="font-bold text-sm">{{ authState.user?.email }}</span>
        <button @click="handleLogout" class="font-bold text-sm hover:text-red-600 transition-colors">
          Đăng xuất
        </button>
      </template>

      <router-link to="/cart" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700 transition-colors flex items-center gap-2">
        <span class="font-bold">{{ cartCount }}</span>
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2 7h14l-2-7M9 21a1 1 0 100-2 1 1 0 000 2zm8 0a1 1 0 100-2 1 1 0 000 2z" />
        </svg>
      </router-link>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { authState, syncAuth } from '../store/auth' 
import { cartState } from '../store/cart'

const router = useRouter()
const cartCount = computed(() => cartState.items.reduce((total, item) => total + item.quantity, 0))

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  syncAuth()
  router.push('/')
}
</script>