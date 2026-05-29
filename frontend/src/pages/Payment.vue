<template>
  <main class="min-h-screen flex items-center justify-center bg-white">
    <div class="flex flex-col md:flex-row w-full max-w-7xl mx-auto overflow-hidden">
      <div class="hidden md:flex flex-col items-center justify-center bg-[#E4002B] w-1/2 p-12 text-white relative">
        <div class="text-left leading-tight">
          <h1 class="text-5xl lg:text-7xl font-black uppercase tracking-tighter">
            THANH TOÁN<br />TẬN TÂM<br />NHANH CHÓNG
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
          <span class="uppercase text-sm text-red-600 font-bold">Thanh toán đơn hàng</span>
          <h2 class="text-3xl font-black uppercase mb-4">Hoàn tất mua sắm</h2>
          <p class="text-sm text-gray-600">Nhập thông tin giao hàng và phương thức thanh toán để xác nhận đơn hàng.</p>
        </div>

        <div v-if="cartItems.length === 0" class="p-8 rounded-3xl border border-gray-200 bg-red-50 text-center">
          <p class="text-xl font-bold text-red-700">Không có sản phẩm nào trong giỏ</p>
          <p class="mt-3 text-gray-600">Quay lại thực đơn để chọn món trước khi thanh toán.</p>
          <router-link to="/menu" class="inline-block mt-6 bg-red-600 text-white px-6 py-3 rounded-full font-bold hover:bg-red-700 transition-colors">
            Đi đến thực đơn
          </router-link>
        </div>

        <div v-else>
          <div v-if="submitted" class="space-y-6">
            <div class="p-8 rounded-3xl border border-green-200 bg-green-50 text-center">
              <p class="text-xl font-bold text-green-700">Thanh toán thành công!</p>
              <p class="mt-3 text-gray-700">Đơn hàng của bạn đã được ghi nhận. Mã đơn hàng của bạn là <span class="font-black">{{ confirmationCode }}</span>.</p>
            </div>
            <div class="p-6 rounded-3xl border border-gray-200 bg-white shadow-sm">
              <h3 class="text-xl font-bold mb-4">Tóm tắt đơn hàng</h3>
              <div class="space-y-3">
                <div v-for="item in submittedItems" :key="item.productId" class="flex justify-between items-center gap-4">
                  <div>
                    <p class="font-semibold">{{ item.name }}</p>
                    <p class="text-sm text-gray-500">Số lượng: {{ item.quantity }}</p>
                  </div>
                  <div class="font-black">{{ formatPrice(item.price * item.quantity) }}</div>
                </div>
              </div>
              <div class="mt-6 border-t border-gray-200 pt-4 flex justify-between text-gray-700 font-bold">
                <span>Tổng thanh toán</span>
                <span>{{ formatPrice(submittedTotal) }}</span>
              </div>
            </div>
            <router-link to="/menu" class="inline-flex items-center justify-center w-full bg-red-600 text-white py-3 rounded-full font-bold hover:bg-red-700 transition-colors">
              Tiếp tục mua sắm
            </router-link>
          </div>

          <div v-else class="space-y-8">
            <form @submit.prevent="submitPayment" class="space-y-6">
              <div class="grid gap-4">
                <label class="block">
                  <span class="text-sm font-bold text-gray-700">Họ và tên</span>
                  <input v-model="form.fullName" type="text" placeholder="Nguyễn Văn A" class="mt-2 w-full rounded-3xl border border-gray-200 px-4 py-3 focus:border-red-500 focus:outline-none" />
                </label>
                <label class="block">
                  <span class="text-sm font-bold text-gray-700">Số điện thoại</span>
                  <input v-model="form.phone" type="tel" placeholder="0933 123 456" class="mt-2 w-full rounded-3xl border border-gray-200 px-4 py-3 focus:border-red-500 focus:outline-none" />
                </label>
                <label class="block">
                  <span class="text-sm font-bold text-gray-700">Địa chỉ giao hàng</span>
                  <input v-model="form.address" type="text" placeholder="Số 10, Đường ABC, Quận 1" class="mt-2 w-full rounded-3xl border border-gray-200 px-4 py-3 focus:border-red-500 focus:outline-none" />
                </label>
              </div>

              <!-- Branch picker - Map -->
              <div>
                <p class="text-sm font-bold text-gray-700 mb-2">Chọn cửa hàng</p>
                <div ref="branchMapEl" class="w-full h-64 rounded-2xl overflow-hidden border border-gray-200 mb-3"></div>
                <div v-if="selectedBranch" class="flex items-start gap-3 rounded-2xl border border-red-200 bg-red-50 px-4 py-3">
                  <span class="text-red-600 mt-0.5">📍</span>
                  <div>
                    <p class="text-sm font-semibold text-red-700">{{ selectedBranch.name }}</p>
                    <p class="text-xs text-gray-500">{{ selectedBranch.address }}</p>
                  </div>
                </div>
                <p v-else class="text-xs text-gray-400 text-center">Nhấn vào một địa điểm trên bản đồ để chọn cửa hàng</p>
              </div>

              <div class="rounded-3xl border border-gray-200 bg-gray-50 p-5">
                <p class="text-sm font-bold text-gray-700 mb-3">Phương thức thanh toán</p>
                <div class="space-y-3">
                  <label
                    class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="form.paymentMethod === 'COD' ? 'border-red-500 bg-red-50' : 'border-transparent bg-white'"
                  >
                    <input type="radio" value="COD" v-model="form.paymentMethod" class="h-4 w-4 accent-red-600" />
                    <span class="text-2xl">🛵</span>
                    <div>
                      <p class="font-semibold text-sm">Thanh toán khi nhận hàng</p>
                      <p class="text-xs text-gray-400">Trả tiền mặt khi giao hàng</p>
                    </div>
                  </label>
                  <label
                    class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="form.paymentMethod === 'MOMO' ? 'border-pink-500 bg-pink-50' : 'border-transparent bg-white'"
                  >
                    <input type="radio" value="MOMO" v-model="form.paymentMethod" class="h-4 w-4 accent-pink-600" />
                    <span class="text-2xl">💜</span>
                    <div>
                      <p class="font-semibold text-sm">MoMo</p>
                      <p class="text-xs text-gray-400">Ví điện tử MoMo</p>
                    </div>
                  </label>
                  <label
                    class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="form.paymentMethod === 'VNPAY' ? 'border-blue-500 bg-blue-50' : 'border-transparent bg-white'"
                  >
                    <input type="radio" value="VNPAY" v-model="form.paymentMethod" class="h-4 w-4 accent-blue-600" />
                    <span class="text-2xl">🏦</span>
                    <div>
                      <p class="font-semibold text-sm">VNPay</p>
                      <p class="text-xs text-gray-400">Thanh toán qua VNPay</p>
                    </div>
                  </label>
                </div>
              </div>

              <div v-if="form.paymentMethod === 'MOMO'" class="rounded-3xl border border-dashed border-yellow-400 bg-yellow-50 px-5 py-4 text-sm text-yellow-700">
                Cổng thanh toán <strong>MoMo</strong> đang được tích hợp. Đơn hàng sẽ được đặt và thanh toán xác nhận sau.
              </div>

              <div class="rounded-3xl border border-gray-200 bg-gray-50 p-6">
                <div class="flex justify-between text-gray-600">
                  <span>Tổng số món</span>
                  <span>{{ totalQuantity }}</span>
                </div>
                <div class="mt-3 flex justify-between text-gray-600">
                  <span>Tổng giá</span>
                  <span>{{ formatPrice(totalPrice) }}</span>
                </div>
              </div>

              <div class="space-y-3">
                <p v-if="error" class="text-sm text-red-700 font-semibold">{{ error }}</p>
                <button type="submit" class="w-full bg-red-600 text-white py-3 rounded-full font-bold hover:bg-red-700 transition-all">
                  Xác nhận thanh toán
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { computed, ref, onMounted, nextTick } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import { cartState, clearCart } from '@/store/cart.js'
import { orderApi } from '@/api/order.js'
import { branchApi } from '@/api/branch.js'

const form = ref({
  fullName: '',
  phone: '',
  address: '',
  paymentMethod: 'COD',
  branchId: ''
})

const branchMapEl = ref(null)
const selectedBranch = ref(null)

onMounted(async () => {
  let branches = []
  try {
    branches = await branchApi.getAllBranches()
  } catch (e) {
    console.error(e)
  }

  await nextTick()
  if (!branchMapEl.value) return

  const map = L.map(branchMapEl.value).setView([10.7626, 106.6602], 12)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map)

  branches.forEach(branch => {
    if (!branch.latitude || !branch.longitude) return
    const marker = L.marker([branch.latitude, branch.longitude])
      .addTo(map)
      .bindPopup(`<b>${branch.name}</b><br><small>${branch.address || ''}</small>`)

    marker.on('click', () => {
      form.value.branchId = branch.branchId
      selectedBranch.value = branch
    })
  })
})
const error = ref('')
const submitted = ref(false)
const confirmationCode = ref('')
const submittedItems = ref([])
const submittedTotal = ref(0)

const cartItems = computed(() => cartState.items)
const totalQuantity = computed(() => cartItems.value.reduce((sum, item) => sum + item.quantity, 0))
const totalPrice = computed(() => cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0))

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
    .format(value).replace('₫', 'đ')
}

const buildConfirmationCode = () => {
  return `KFC-${Math.random().toString(36).substr(2, 6).toUpperCase()}`
}

const validateForm = () => {
  if (!form.value.fullName.trim() || !form.value.phone.trim() || !form.value.address.trim()) {
    error.value = 'Vui lòng điền đầy đủ thông tin liên hệ và địa chỉ.'
    return false
  }
  if (!form.value.branchId) {
    error.value = 'Vui lòng chọn cửa hàng.'
    return false
  }
  return true
}

const submitPayment = async () => {
  error.value = ''
  if (!validateForm()) {
    return
  }

  try {
    const orderPayload = {
      items: cartItems.value.map((item) => ({
        productId: item.productId,
        quantity: item.quantity
      })),
      paymentMethod: form.value.paymentMethod,
      fullName: form.value.fullName,
      phone: form.value.phone,
      address: form.value.address,
      branchId: form.value.branchId,
      totalAmount: totalPrice.value
    }

    const result = await orderApi.createOrder(orderPayload)

    if (form.value.paymentMethod === 'VNPAY') {
      const token = sessionStorage.getItem('token')
      const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/payment/vnpay/create`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
        body: JSON.stringify({ orderId: result.orderId })
      })
      if (!res.ok) throw new Error('Không thể tạo liên kết thanh toán VNPay')
      const { paymentUrl } = await res.json()
      clearCart()
      window.location.href = paymentUrl
      return
    }

    submittedItems.value = cartItems.value.map((item) => ({ ...item }))
    submittedTotal.value = totalPrice.value
    confirmationCode.value = result.orderId || buildConfirmationCode()
    clearCart()
    submitted.value = true
  } catch (err) {
    error.value = err.message || 'Không thể tạo đơn hàng. Vui lòng thử lại.'
  }
}
</script>
