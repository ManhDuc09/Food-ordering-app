<template>
  <main class="min-h-screen flex items-center justify-center bg-white">
    <div class="flex flex-col md:flex-row w-full max-w-7xl mx-auto overflow-hidden">

      <!-- Left Red Panel -->
      <div class="hidden md:flex flex-col items-center justify-center bg-[#E4002B] w-1/2 p-12 text-white">
        <div class="text-left leading-tight">
          <h1 class="text-5xl lg:text-7xl font-black uppercase tracking-tighter">
            TẠO<br />
            TÀI<br />
            KHOẢN<br />
            NGAY!
          </h1>
        </div>
        <div class="mt-12">
          <div class="bg-white p-4 rounded-sm">
            <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" alt="KFC Logo" class="w-24" />
          </div>
        </div>
      </div>

      <!-- Right Form Panel -->
      <div class="w-full md:w-1/2 flex flex-col justify-center px-8 py-12 lg:px-24 bg-white">
        <h2 class="text-3xl font-black uppercase mb-8">TẠO TÀI KHOẢN</h2>

        <form @submit.prevent="handleRegister" class="space-y-6">

          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Họ của bạn *</label>
            <input
              v-model="lastName"
              type="text"
              class="w-full py-2 outline-none transition-colors"
              required
            />
          </div>

          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Tên của bạn *</label>
            <input
              v-model="firstName"
              type="text"
              class="w-full py-2 outline-none transition-colors"
              required
            />
          </div>

          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Số điện thoại *</label>
            <input
              v-model="phone"
              type="tel"
              class="w-full py-2 outline-none transition-colors"
              required
            />
          </div>

          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Địa chỉ email của bạn *</label>
            <input
              v-model="email"
              type="email"
              class="w-full py-2 outline-none transition-colors"
              required
            />
          </div>

          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Mật khẩu *</label>
            <div class="flex items-center">
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                class="w-full py-2 outline-none transition-colors"
                required
              />
              <button type="button" @click="showPassword = !showPassword" class="text-gray-400 hover:text-black">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.644m17.928.644a1.012 1.012 0 0 1 0-.644M12 18.75a6.75 6.75 0 1 0 0-13.5 6.75 6.75 0 0 0 0 13.5ZM12 15a3 3 0 1 1 0-6 3 3 0 0 1 0 6Z" />
                </svg>
              </button>
            </div>
          </div>

          <div class="flex items-start gap-3">
            <input
              v-model="agreed"
              type="checkbox"
              class="mt-1 w-4 h-4 accent-[#E4002B]"
              required
            />
            <label class="text-sm text-gray-600">
              Tôi đã đọc và đồng ý với các
              <a href="#" class="font-bold underline">Chính Sách Hoạt Động</a>
              và
              <a href="#" class="font-bold underline">Chính Sách Bảo Mật Thông Tin của KFC VietNam.</a>
            </label>
          </div>

          <button
            type="submit"
            :disabled="loading"
            class="w-full bg-[#E4002B] hover:bg-red-700 text-white font-bold py-3 rounded-full shadow-lg transition-all disabled:opacity-50"
          >
            {{ loading ? 'Đang tạo...' : 'Tạo Tài Khoản' }}
          </button>

        </form>

        <p class="mt-8 text-center text-sm text-gray-600">
          Bạn đã có tài khoản?
          <router-link to="/login" class="font-bold underline text-black">Đăng nhập</router-link>
        </p>
      </div>

    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api/auth'
import { showToast } from '../store/toast'
const router = useRouter()

const firstName = ref('')
const lastName = ref('')
const phone = ref('')
const email = ref('')
const password = ref('')
const showPassword = ref(false)
const agreed = ref(false)
const loading = ref(false)

const handleRegister = async () => {
  loading.value = true
  try {
    const data = await authApi.register({
      email: email.value,
      password: password.value,
      fullName: `${lastName.value} ${firstName.value}`,
      phoneNumber: phone.value
    })

    localStorage.setItem('user', JSON.stringify({ email: data.email, role: data.role }))
    localStorage.setItem('expiresAt', data.expiresAt)

    showToast('Đăng ký thành công! Chào mừng bạn!')
    router.push('/')
  } catch (err) {
    showToast(err.message || 'Đăng ký thất bại, vui lòng thử lại.', 'error')
  } finally {
    loading.value = false
  }
}
</script>