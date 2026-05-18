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
        <div v-if="error" class="mb-6 p-4 bg-red-50 border-l-4 border-red-600 text-red-700 text-sm font-bold animate-pulse">
           <div class="flex items-center">
             <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
               <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
             </svg>
             {{ error }}
           </div>
        </div>

        <div v-if="successMessage" class="mb-6 p-4 bg-green-50 border-l-4 border-green-600 text-green-700 text-sm font-bold">
           <div class="flex items-center">
             <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
               <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
             </svg>
             {{ successMessage }}
           </div>
        </div>
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
import { syncAuth } from '../store/auth'
const router = useRouter()

const email = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)
const error = ref('')


const successMessage = ref('') 

const handleLogin = async () => {
  error.value = ''
  successMessage.value = ''
  loading.value = true

  try {
    const data = await authApi.login(email.value, password.value)
    
    
    successMessage.value = "Đăng nhập thành công! Đang chuyển hướng..."
    
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify({ email: data.email, role: data.role }))
    
    syncAuth()

    setTimeout(() => {
      const redirectPath = router.currentRoute.value.query.redirect || '/'
      router.push(redirectPath)
    }, 1500)

  } catch (err) {
    error.value = err.response?.data?.message || err.message || "Email hoặc mật khẩu không đúng!"
  } finally {
    loading.value = false
  }
}
</script>