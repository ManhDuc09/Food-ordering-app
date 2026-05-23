<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import MainLayout from './layouts/MainLayout.vue'
import { authState, checkAndHandleExpiry } from './store/auth'

const router = useRouter()

const dismissExpired = () => {
  authState.sessionExpired = false
  router.push('/login')
}

let expiryInterval
onMounted(() => {
  expiryInterval = setInterval(() => {
    if (authState.isLoggedIn) checkAndHandleExpiry()
  }, 30000)
})
onUnmounted(() => clearInterval(expiryInterval))
</script>

<template>
  <MainLayout />

  <Transition name="fade">
    <div
      v-if="authState.sessionExpired"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-[9999] px-4"
    >
      <div class="bg-white rounded-2xl p-8 max-w-sm w-full shadow-2xl text-center">
        <div class="text-5xl mb-4">⏰</div>
        <h2 class="text-xl font-bold mb-2">Phiên đăng nhập đã hết hạn</h2>
        <p class="text-gray-500 text-sm mb-6">Vui lòng đăng nhập lại để tiếp tục.</p>
        <button
          @click="dismissExpired"
          class="w-full bg-red-600 text-white font-bold py-3 rounded-full hover:bg-red-700 transition-colors"
        >
          Đăng nhập lại
        </button>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
