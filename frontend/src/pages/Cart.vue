<template>
  <main class="min-h-screen flex items-center justify-center bg-white">
    <div class="flex flex-col md:flex-row w-full max-w-7xl mx-auto overflow-hidden">
      <div class="hidden md:flex flex-col items-center justify-center bg-[#E4002B] w-1/2 p-12 text-white relative">
        <div class="text-left leading-tight">
          <h1 class="text-5xl lg:text-7xl font-black uppercase tracking-tighter">
            GIỎ HÀNG<br />
            TRONG RẠNG<br />
            NGAY LẬP TỨC
          </h1>
        </div>
        <div class="mt-12">
          <div class="bg-white p-4 rounded-sm">
            <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" alt="KFC Logo" class="w-24" />
          </div>
        </div>
      </div>

      <div class="w-full md:w-1/2 flex flex-col justify-center px-8 py-12 lg:px-24 bg-white">
        <div class="mb-8">
          <span class="uppercase text-sm text-red-600 font-bold">Giỏ hàng của bạn</span>
          <h2 class="text-3xl font-black uppercase mb-4">Xem lại đơn hàng</h2>
          <p class="text-sm text-gray-600">Kiểm tra sản phẩm, số lượng và tổng giá trước khi thanh toán.</p>
        </div>

        <div v-if="cartItems.length === 0" class="p-8 rounded-3xl border border-gray-200 bg-red-50 text-center">
          <p class="text-xl font-bold text-red-700">Giỏ hàng trống</p>
          <p class="mt-3 text-gray-600">Thêm món vào giỏ rồi quay lại trang này để thanh toán.</p>
          <router-link to="/menu" class="inline-block mt-6 bg-red-600 text-white px-6 py-3 rounded-full font-bold hover:bg-red-700 transition-colors">
            Đi đến thực đơn
          </router-link>
        </div>

        <div v-else class="space-y-6">
          <div class="space-y-4">
            <div v-for="item in cartItems" :key="item.productId" class="flex items-center gap-4 p-4 rounded-3xl border border-gray-200 shadow-sm">
              <img :src="item.imageUrl || 'https://placehold.co/120x120?text=No+Image'" alt="item.name" class="w-24 h-24 rounded-3xl object-cover" />
              <div class="flex-1">
                <p class="font-bold text-lg">{{ item.name }}</p>
                <p class="text-sm text-gray-500">Số lượng: {{ item.quantity }}</p>
                <p class="text-sm text-gray-500">Giá mỗi chiếc: {{ formatPrice(item.price) }}</p>
              </div>
              <div class="text-right font-black text-lg">{{ formatPrice(item.price * item.quantity) }}</div>
            </div>
          </div>

          <div class="flex flex-col gap-3 p-6 rounded-3xl border border-gray-200 bg-gray-50">
            <div class="flex justify-between text-gray-600">
              <span>Tổng số món</span>
              <span>{{ totalQuantity }}</span>
            </div>
            <div class="flex justify-between text-gray-600">
              <span>Tổng giá</span>
              <span>{{ formatPrice(totalPrice) }}</span>
            </div>
            <button @click="goToPayment" class="w-full bg-red-600 text-white py-3 rounded-full font-bold hover:bg-red-700 transition-all">
              Thanh toán
            </button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { cartState } from '@/store/cart.js'

const router = useRouter()
const cartItems = computed(() => cartState.items)
const totalQuantity = computed(() => cartItems.value.reduce((sum, item) => sum + item.quantity, 0))
const totalPrice = computed(() => cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0))

const goToPayment = () => {
  router.push('/payment')
}

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
    .format(value).replace('₫', 'đ')
}
</script>
