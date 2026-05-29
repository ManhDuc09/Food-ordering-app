<template>
  <main class="min-h-screen flex items-center justify-center bg-gray-50 p-6">
    <div class="bg-white rounded-2xl shadow-sm p-10 w-full max-w-md text-center">

      <!-- Loading -->
      <div v-if="loading">
        <svg class="animate-spin w-12 h-12 text-[#E4002B] mx-auto mb-4" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
        </svg>
        <p class="font-bold text-gray-500">Đang xác nhận thanh toán...</p>
      </div>

      <!-- Success -->
      <div v-else-if="result.success">
        <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h2 class="text-2xl font-black text-gray-900 mb-2">Thanh toán thành công!</h2>
        <p class="text-gray-500 text-sm mb-1">Đơn hàng của bạn đã được xác nhận.</p>
        <p class="text-xs text-gray-400 mb-6">Mã đơn: <span class="font-black text-gray-700">{{ shortId }}</span></p>
        <div class="flex gap-3">
          <router-link to="/profile" class="flex-1 border border-gray-300 rounded-full py-2 text-sm font-bold text-gray-600 hover:bg-gray-50">
            Xem đơn hàng
          </router-link>
          <router-link to="/menu" class="flex-1 bg-[#E4002B] text-white rounded-full py-2 text-sm font-bold hover:bg-red-700">
            Tiếp tục mua
          </router-link>
        </div>
      </div>

      <!-- Failed -->
      <div v-else>
        <div class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-[#E4002B]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </div>
        <h2 class="text-2xl font-black text-gray-900 mb-2">Thanh toán thất bại</h2>
        <p class="text-gray-500 text-sm mb-6">
          {{ responseMessage }}
        </p>
        <router-link to="/menu" class="inline-block w-full bg-[#E4002B] text-white rounded-full py-2 text-sm font-bold hover:bg-red-700">
          Quay lại
        </router-link>
      </div>

    </div>
  </main>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const loading = ref(true)
const result = ref({ success: false, responseCode: '', orderId: '' })

const shortId = computed(() => result.value.orderId?.substring(0, 8).toUpperCase() || '')

const responseMessage = computed(() => {
  const codes = {
    '07': 'Giao dịch bị nghi ngờ gian lận.',
    '09': 'Thẻ chưa đăng ký dịch vụ InternetBanking.',
    '10': 'Xác thực thông tin thẻ quá 3 lần.',
    '11': 'Đã hết hạn chờ thanh toán.',
    '12': 'Thẻ/Tài khoản bị khóa.',
    '13': 'Sai mật khẩu OTP.',
    '24': 'Giao dịch bị hủy.',
    '51': 'Tài khoản không đủ số dư.',
    '65': 'Tài khoản vượt hạn mức giao dịch trong ngày.',
    '75': 'Ngân hàng đang bảo trì.',
    '79': 'Sai mật khẩu thanh toán quá số lần.',
  }
  return codes[result.value.responseCode] || 'Giao dịch không thành công. Vui lòng thử lại.'
})

onMounted(async () => {
  const params = Object.fromEntries(new URLSearchParams(window.location.search))
  try {
    const query = new URLSearchParams(params).toString()
    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/payment/vnpay/verify?${query}`)
    result.value = await res.json()
  } catch (e) {
    result.value = { success: false, responseCode: '', orderId: '' }
  } finally {
    loading.value = false
  }
})
</script>
