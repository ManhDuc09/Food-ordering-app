<template>
  <main class="min-h-screen flex items-center justify-center bg-white">
    <div class="flex flex-col md:flex-row w-full max-w-7xl mx-auto overflow-hidden">
      
      <div class="hidden md:flex flex-col items-center justify-center bg-[#E4002B] w-1/2 p-12 text-white relative">
        <div class="text-left leading-tight">
          <h1 class="text-5xl lg:text-7xl font-black uppercase tracking-tighter">
            ĐẶT MÓN<br />
            DỄ DÀNG<br />
            GIAO HÀNG<br />
            NHANH CHÓNG
          </h1>
        </div>
        <div class="mt-12">
          <div class="bg-white p-4 rounded-sm">
            <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" alt="KFC Logo" class="w-24" />
          </div>
        </div>
      </div>
 
      <div class="w-full md:w-1/2 flex flex-col justify-center px-8 py-12 lg:px-24 bg-white">
        <h2 class="text-3xl font-black uppercase mb-8 italic">LOGIN</h2>

        <form @submit.prevent="handleLogin" class="space-y-6">
          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Email *</label>
            <input
                  v-model="email"
                  type="email"
                  class="w-full py-2 outline-none focus:border-black transition-colors"
                />
          </div>

          <div class="relative border-b border-gray-300">
            <label class="text-xs text-gray-500 uppercase font-bold">Password *</label>
            <div class="flex items-center">
              <input
                    v-model="password"
                    :type="showPassword ? 'text' : 'password'"
                    class="w-full py-2 outline-none focus:border-black transition-colors"
                  />
              <button type="button" @click="showPassword = !showPassword" class="text-gray-400 hover:text-black">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.644m17.928.644a1.012 1.012 0 0 1 0-.644M12 18.75a6.75 6.75 0 1 0 0-13.5 6.75 6.75 0 0 0 0 13.5ZM12 15a3 3 0 1 1 0-6 3 3 0 0 1 0 6Z" />
                </svg>
              </button>
            </div>
          </div>

          <div class="text-right">
            <a href="#" class="text-xs font-bold underline">Forgot Your Password?</a>
          </div>

          <button class="w-full bg-[#28a745] hover:bg-[#218838] text-white font-bold py-3 rounded-full shadow-lg transition-all" type="submit">
            Login
          </button>
        </form>

        <div class="mt-8">
          <div class="relative flex py-5 items-center">
              <div class="flex-grow border-t border-gray-200"></div>
              <span class="flex-shrink mx-4 text-gray-500 text-sm font-bold">Or continue with</span>
              <div class="flex-grow border-t border-gray-200"></div>
          </div>

          <div class="space-y-3">
            <button class="w-full bg-black text-white flex items-center justify-center py-3 rounded-full font-bold hover:opacity-90">
              <span class="mr-2"></span> Sign in with Apple
            </button>
            <button class="w-full bg-[#DB4437] text-white flex items-center justify-center py-3 rounded-full font-bold hover:opacity-90">
              <img src="https://www.gstatic.com/images/branding/product/1x/gsa_512dp.png" class="w-5 h-5 mr-2 bg-white rounded-full p-0.5" /> 
              Sign in with Google
            </button>
          </div>
        </div>

        <p class="mt-8 text-center text-sm text-gray-600">
          Haven't got an account? 
          <a href="#" class="font-bold underline text-black">Sign Up</a>
        </p>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api/auth'
const router = useRouter()

const email = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  error.value = ''
  loading.value = true

  try {
    const data = await authApi.login(email.value, password.value)
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify({ email: data.email, role: data.role }))
    router.push('/')

  } catch (err) {
    error.value = err.message

  } finally {
    loading.value = false   
  }
}
</script>