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
                  <input
                    v-model="form.fullName" type="text" placeholder="Nguyễn Văn A"
                    class="mt-2 w-full rounded-3xl border px-4 py-3 focus:outline-none transition-colors"
                    :class="fieldErrors.fullName ? 'border-red-400 bg-red-50' : 'border-gray-200 focus:border-red-500'"
                    @blur="touchField('fullName')"
                  />
                  <p v-if="fieldErrors.fullName" class="mt-1 text-xs text-red-500 px-2">{{ fieldErrors.fullName }}</p>
                </label>

                <label class="block">
                  <span class="text-sm font-bold text-gray-700">Số điện thoại</span>
                  <input
                    v-model="form.phone" type="tel" placeholder="0912 345 678"
                    class="mt-2 w-full rounded-3xl border px-4 py-3 focus:outline-none transition-colors"
                    :class="fieldErrors.phone ? 'border-red-400 bg-red-50' : 'border-gray-200 focus:border-red-500'"
                    @blur="touchField('phone')"
                  />
                  <p v-if="fieldErrors.phone" class="mt-1 text-xs text-red-500 px-2">{{ fieldErrors.phone }}</p>
                </label>

                <div class="block">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-sm font-bold text-gray-700">Địa chỉ giao hàng</span>
                    <router-link to="/profile" target="_blank"
                      class="text-xs text-red-600 font-bold hover:underline flex items-center gap-1">
                      + Quản lý địa chỉ
                    </router-link>
                  </div>

                  <!-- No saved addresses -->
                  <p v-if="savedAddresses.length === 0" class="text-xs text-gray-400 mb-2">
                    Bạn chưa có địa chỉ nào.
                    <router-link to="/profile" target="_blank" class="text-red-500 underline">Thêm trong hồ sơ</router-link>
                  </p>

                  <!-- Address cards -->
                  <div v-if="savedAddresses.length > 0" class="space-y-2 mb-2">
                    <label
                      v-for="addr in savedAddresses" :key="addr.id"
                      class="flex items-start gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                      :class="selectedAddressId === addr.id ? 'border-red-500 bg-red-50' : 'border-gray-100 bg-white hover:border-gray-300'"
                    >
                      <input type="radio" :value="addr.id" v-model="selectedAddressId" class="mt-0.5 h-4 w-4 accent-red-600 shrink-0" />
                      <div class="flex-1 min-w-0">
                        <p class="text-sm font-semibold text-gray-800">{{ addr.street }}</p>
                        <p v-if="addr.city" class="text-xs text-gray-500">{{ addr.city }}</p>
                        <span v-if="addr.isDefault" class="text-xs font-bold text-red-500">Mặc định</span>
                        <span v-if="!addr.latitude || !addr.longitude" class="text-xs text-orange-400">Chưa có toạ độ — phí tạm ước tính</span>
                      </div>
                    </label>

                    <!-- Custom address option -->
                    <label
                      class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 border-dashed px-4 py-3 transition-colors"
                      :class="selectedAddressId === 'custom' ? 'border-red-400 bg-red-50' : 'border-gray-200 hover:border-gray-300'"
                    >
                      <input type="radio" value="custom" v-model="selectedAddressId" class="h-4 w-4 accent-red-600 shrink-0" />
                      <span class="text-sm text-gray-500 font-semibold">Nhập địa chỉ khác...</span>
                    </label>
                  </div>

                  <!-- Manual input (no saved addresses OR custom selected) -->
                  <input
                    v-if="savedAddresses.length === 0 || selectedAddressId === 'custom'"
                    v-model="form.address" type="text" placeholder="Số 10, Đường ABC, Quận 1"
                    class="w-full rounded-3xl border px-4 py-3 focus:outline-none transition-colors"
                    :class="fieldErrors.address ? 'border-red-400 bg-red-50' : 'border-gray-200 focus:border-red-500'"
                    @blur="touchField('address')"
                  />
                  <p v-if="fieldErrors.address" class="mt-1 text-xs text-red-500 px-2">{{ fieldErrors.address }}</p>
                </div>
              </div>

              <!-- Branch picker -->
              <div>
                <p class="text-sm font-bold text-gray-700 mb-2">Chọn cửa hàng</p>

                <!-- Map -->
                <div ref="branchMapEl" class="w-full h-48 rounded-2xl overflow-hidden border border-gray-200 mb-3"></div>

                <!-- Search -->
                <div class="relative mb-2">
                  <svg class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35M17 11A6 6 0 115 11a6 6 0 0112 0z" />
                  </svg>
                  <input
                    v-model="branchSearch" type="text" placeholder="Tìm chi nhánh..."
                    class="w-full rounded-2xl border border-gray-200 pl-9 pr-4 py-2.5 text-sm focus:outline-none focus:border-red-400"
                  />
                </div>

                <!-- Branch list -->
                <div class="space-y-2 max-h-52 overflow-y-auto pr-0.5">
                  <button
                    v-for="branch in filteredBranches" :key="branch.branchId"
                    type="button"
                    @click="selectBranch(branch)"
                    class="w-full flex items-start gap-3 text-left rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="selectedBranch?.branchId === branch.branchId
                      ? 'border-red-500 bg-red-50'
                      : 'border-gray-100 bg-white hover:border-gray-300'"
                  >
                    <span class="shrink-0 w-9 h-9 rounded-lg bg-red-50 flex items-center justify-center">
                      <svg class="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.8" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.8" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                      </svg>
                    </span>
                    <div class="min-w-0 flex-1">
                      <p class="text-sm font-semibold text-gray-800">{{ branch.name }}</p>
                      <p class="text-xs text-gray-500 truncate">{{ branch.address }}</p>
                    </div>
                    <div class="shrink-0 ml-2 flex items-center gap-2">
                      <span v-if="branch._dist !== null" class="text-xs font-medium" :class="selectedBranch?.branchId === branch.branchId ? 'text-red-400' : 'text-gray-400'">
                        {{ branch._dist < 1 ? (branch._dist * 1000).toFixed(0) + 'm' : branch._dist.toFixed(1) + 'km' }}
                      </span>
                      <svg v-if="selectedBranch?.branchId === branch.branchId"
                        class="w-4 h-4 text-red-500" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                      </svg>
                    </div>
                  </button>
                  <p v-if="filteredBranches.length === 0" class="text-center text-sm text-gray-400 py-4">
                    Không tìm thấy chi nhánh
                  </p>
                </div>
                <p v-if="userLocation" class="text-xs text-gray-400 mt-2 text-center">
                  Chỉ hiển thị chi nhánh trong bán kính 10km
                </p>
              </div>

              <div class="rounded-3xl border border-gray-200 bg-gray-50 p-5">
                <p class="text-sm font-bold text-gray-700 mb-3">Phương thức thanh toán</p>
                <div class="space-y-3">
                  <label
                    class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="form.paymentMethod === PaymentMethod.COD ? 'border-red-500 bg-red-50' : 'border-transparent bg-white'"
                  >
                    <input type="radio" :value="PaymentMethod.COD" v-model="form.paymentMethod" class="h-4 w-4 accent-red-600" />
                    <!-- Cash icon -->
                    <span class="shrink-0 w-9 h-9 rounded-xl bg-green-100 flex items-center justify-center">
                      <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <rect x="2" y="6" width="20" height="12" rx="2" stroke-width="1.8"/>
                        <circle cx="12" cy="12" r="3" stroke-width="1.8"/>
                        <path stroke-width="1.8" stroke-linecap="round" d="M6 10v4M18 10v4"/>
                      </svg>
                    </span>
                    <div>
                      <p class="font-semibold text-sm">Thanh toán khi nhận hàng</p>
                      <p class="text-xs text-gray-400">Trả tiền mặt khi giao hàng</p>
                    </div>
                  </label>
                  <label
                    class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="form.paymentMethod === PaymentMethod.MOMO ? 'border-pink-500 bg-pink-50' : 'border-transparent bg-white'"
                  >
                    <input type="radio" :value="PaymentMethod.MOMO" v-model="form.paymentMethod" class="h-4 w-4 accent-pink-600" />
                    <!-- MoMo logo -->
                    <span class="shrink-0 w-9 h-9 rounded-xl overflow-hidden">
                      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAvqtnrX-jan83zUzhldZsePVib6MTkkhXOQ&s" alt="MoMo" class="w-full h-full object-cover" />
                    </span>
                    <div>
                      <p class="font-semibold text-sm">MoMo</p>
                      <p class="text-xs text-gray-400">Ví điện tử MoMo</p>
                    </div>
                  </label>
                  <label
                    class="flex items-center gap-3 cursor-pointer rounded-2xl border-2 px-4 py-3 transition-colors"
                    :class="form.paymentMethod === PaymentMethod.VNPAY ? 'border-blue-500 bg-blue-50' : 'border-transparent bg-white'"
                  >
                    <input type="radio" :value="PaymentMethod.VNPAY" v-model="form.paymentMethod" class="h-4 w-4 accent-blue-600" />
                    
                    <span class="shrink-0 w-9 h-9 rounded-xl overflow-hidden">
                      <img src="https://yt3.googleusercontent.com/JM1m2wng0JQUgSg9ZSEvz7G4Rwo7pYb4QBYip4PAhvGRyf1D_YTbL2DdDjOy0qOXssJPdz2r7Q=s900-c-k-c0x00ffffff-no-rj" alt="VNPay" class="w-full h-full object-contain bg-white p-0.5" />
                    </span>
                    <div>
                      <p class="font-semibold text-sm">VNPay</p>
                      <p class="text-xs text-gray-400">Thanh toán qua VNPay</p>
                    </div>
                  </label>
                </div>
              </div>

              <div v-if="form.paymentMethod === PaymentMethod.MOMO" class="rounded-3xl border border-dashed border-yellow-400 bg-yellow-50 px-5 py-4 text-sm text-yellow-700">
                Cổng thanh toán <strong>MoMo</strong> đang được tích hợp. Đơn hàng sẽ được đặt và thanh toán xác nhận sau.
              </div>

              <div class="rounded-3xl border border-gray-200 bg-gray-50 p-6 space-y-3">
                <div class="flex justify-between text-gray-600 text-sm">
                  <span>Tổng số món</span>
                  <span>{{ totalQuantity }}</span>
                </div>
                <div class="flex justify-between text-gray-600 text-sm">
                  <span>Tiền hàng</span>
                  <span>{{ formatPrice(totalPrice) }}</span>
                </div>

                <!-- Shipping -->
                <div class="flex justify-between text-sm"
                  :class="shippingFee === -1 || shippingFee === -2 ? 'text-red-500' : 'text-gray-600'">
                  <span class="flex items-center gap-1">
                    Phí giao hàng
                    <span v-if="deliveryDistance !== null" class="text-xs text-gray-400">(~{{ deliveryDistance.toFixed(1) }}km)</span>
                  </span>
                  <span v-if="calculatingShipping" class="text-xs text-gray-400 animate-pulse">Đang tính...</span>
                  <span v-else-if="shippingFee === -1" class="font-semibold">Ngoài phạm vi</span>
                  <span v-else-if="shippingFee === -2" class="font-semibold text-xs">Không xác định được địa chỉ</span>
                  <span v-else-if="!selectedBranch || !form.address" class="text-xs text-gray-400">Chọn địa chỉ & chi nhánh</span>
                  <span v-else>{{ formatPrice(shippingFee) }}</span>
                </div>

                <div class="border-t border-gray-200 pt-3 flex justify-between font-bold text-gray-800">
                  <span>Tổng thanh toán</span>
                  <span class="text-[#E4002B]">{{ formatPrice(totalWithShipping) }}</span>
                </div>
              </div>

              <p v-if="shippingFee === -1" class="text-sm text-red-600 font-semibold text-center">
                Địa chỉ quá xa chi nhánh (tối đa 15km). Vui lòng chọn chi nhánh gần hơn.
              </p>

              <button type="submit" class="w-full bg-red-600 text-white py-3 rounded-full font-bold hover:bg-red-700 transition-all">
                Xác nhận thanh toán
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { computed, ref, watch, onMounted, nextTick } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import { cartState, clearCart } from '@/store/cart.js'
import { orderApi } from '@/api/order.js'
import { branchApi } from '@/api/branch.js'
import { profileApi } from '@/api/profile.js'
import { showToast } from '@/store/toast.js'
import { PaymentMethod } from '@/constants/enums.js'

const form = ref({
  fullName: '',
  phone: '',
  address: '',
  paymentMethod: PaymentMethod.COD,
  branchId: ''
})

const savedAddresses = ref([])
const selectedAddressId = ref(null)
const fieldErrors = ref({ fullName: '', phone: '', address: '' })

watch(selectedAddressId, async (id) => {
  if (id && id !== 'custom') {
    const addr = savedAddresses.value.find(a => a.id === id)
    if (addr) {
      form.value.address = `${addr.street}${addr.city ? ', ' + addr.city : ''}`
      if (addr.latitude != null && addr.longitude != null && !isNaN(Number(addr.latitude))) {
        addressCoords.value = { lat: Number(addr.latitude), lng: Number(addr.longitude) }
      } else {
        addressCoords.value = await geocodeAddress(form.value.address)
      }
    }
  } else if (id === 'custom') {
    form.value.address = ''
    addressCoords.value = null
  }
})

const branchMapEl = ref(null)
const selectedBranch = ref(null)
const allBranches = ref([])
const branchSearch = ref('')
const mapInstance = ref(null)
const userLocation = ref(null)
const addressCoords = ref(null)
const branchMarkers = new Map()

const branchesWithDistance = computed(() => {
  const loc = addressCoords.value ?? userLocation.value
  return allBranches.value
    .map(b => ({
      ...b,
      _dist: (loc && b.latitude && b.longitude)
        ? haversine(loc.lat, loc.lng, Number(b.latitude), Number(b.longitude))
        : null
    }))
    .sort((a, b) => {
      if (a._dist === null && b._dist === null) return 0
      if (a._dist === null) return 1
      if (b._dist === null) return -1
      return a._dist - b._dist
    })
})

const filteredBranches = computed(() => {
  const q = branchSearch.value.trim().toLowerCase()
  const loc = addressCoords.value ?? userLocation.value
  return branchesWithDistance.value.filter(b => {
    if (!b.isOpen) return false
    if (loc && b._dist !== null && b._dist > 10) return false
    if (q) return b.name?.toLowerCase().includes(q) || b.address?.toLowerCase().includes(q)
    return true
  })
})

const selectBranch = (branch) => {
  form.value.branchId = branch.branchId
  selectedBranch.value = branch
  if (mapInstance.value && branch.latitude && branch.longitude) {
    mapInstance.value.setView([branch.latitude, branch.longitude], 15)
  }
}

watch(filteredBranches, (filtered) => {
  if (!mapInstance.value) return
  const visibleIds = new Set(filtered.map(b => b.branchId))
  branchMarkers.forEach((marker, branchId) => {
    if (visibleIds.has(branchId)) {
      if (!mapInstance.value.hasLayer(marker)) marker.addTo(mapInstance.value)
    } else {
      if (mapInstance.value.hasLayer(marker)) mapInstance.value.removeLayer(marker)
    }
  })
})

onMounted(async () => {
  // Load profile for autofill
  try {
    const profile = await profileApi.getProfile()
    if (profile.fullName) form.value.fullName = profile.fullName
    if (profile.phoneNumber) form.value.phone = profile.phoneNumber
  } catch {}

  // Load saved addresses
  try {
    savedAddresses.value = await profileApi.getAddresses()
    const def = savedAddresses.value.find(a => a.isDefault) || savedAddresses.value[0]
    if (def) selectedAddressId.value = def.id
  } catch {}

  // Load branches for map
  try {
    allBranches.value = await branchApi.getAllBranches()
  } catch (e) {
    console.error(e)
  }

  // Get user location for distance sorting
  navigator.geolocation?.getCurrentPosition(
    pos => { userLocation.value = { lat: pos.coords.latitude, lng: pos.coords.longitude } },
    () => {}
  )

  await nextTick()
  if (!branchMapEl.value) return

  const map = L.map(branchMapEl.value).setView([10.7626, 106.6602], 12)
  mapInstance.value = map
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map)

  allBranches.value.forEach(branch => {
    if (!branch.latitude || !branch.longitude) return
    const marker = L.marker([branch.latitude, branch.longitude])
      .addTo(map)
      .bindPopup(`<b>${branch.name}</b><br><small>${branch.address || ''}</small>`)
    marker.on('click', () => selectBranch(branch))
    branchMarkers.set(branch.branchId, marker)
  })
})

const submitted = ref(false)
const confirmationCode = ref('')
const submittedItems = ref([])
const submittedTotal = ref(0)

const cartItems = computed(() => cartState.items)
const totalQuantity = computed(() => cartItems.value.reduce((sum, item) => sum + item.quantity, 0))
const totalPrice = computed(() => cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0))

// ── Shipping ────────────────────────────────────────────────
const shippingFee = ref(0)
const deliveryDistance = ref(null)
const calculatingShipping = ref(false)

const SHIPPING_TIERS = [
  { maxKm: 3,  fee: 15000 },
  { maxKm: 6,  fee: 25000 },
  { maxKm: 10, fee: 35000 },
  { maxKm: 15, fee: 45000 },
]

const totalWithShipping = computed(() =>
  totalPrice.value + (shippingFee.value > 0 ? shippingFee.value : 0)
)

function haversine(lat1, lng1, lat2, lng2) {
  const R = 6371
  const toRad = d => d * Math.PI / 180
  const dLat = toRad(lat2 - lat1)
  const dLng = toRad(lng2 - lng1)
  const a = Math.sin(dLat / 2) ** 2 +
            Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(dLng / 2) ** 2
  return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
}

async function geocodeAddress(address) {
  try {
    const res = await fetch(
      `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(address)}&format=json&limit=1&countrycodes=vn`,
      { headers: { 'Accept-Language': 'vi' } }
    )
    const data = await res.json()
    if (!Array.isArray(data) || !data.length) return null
    return { lat: parseFloat(data[0].lat), lng: parseFloat(data[0].lon) }
  } catch {
    return null
  }
}

let shippingTimer = null
async function calculateShipping() {
  if (!selectedBranch.value?.latitude || !form.value.address) {
    shippingFee.value = 0
    deliveryDistance.value = null
    return
  }
  calculatingShipping.value = true
  try {
    const savedAddr = savedAddresses.value.find(a => a.id === selectedAddressId.value)
    let coords = null
    if (savedAddr?.latitude != null && savedAddr?.longitude != null) {
      coords = { lat: Number(savedAddr.latitude), lng: Number(savedAddr.longitude) }
    }
    if (!coords || isNaN(coords.lat) || isNaN(coords.lng)) {
      coords = await geocodeAddress(form.value.address)
    }
    // Last resort: use current GPS position as a proxy for the delivery address
    if (!coords && userLocation.value) {
      coords = { lat: userLocation.value.lat, lng: userLocation.value.lng }
    }
    if (!coords) {
      shippingFee.value = -2
      deliveryDistance.value = null
      return
    }

    const dist = haversine(
      Number(selectedBranch.value.latitude), Number(selectedBranch.value.longitude),
      coords.lat, coords.lng
    )
    if (isNaN(dist)) { shippingFee.value = -2; deliveryDistance.value = null; return }
    deliveryDistance.value = dist

    if (dist > 15) {
      shippingFee.value = -1
    } else {
      shippingFee.value = (SHIPPING_TIERS.find(t => dist <= t.maxKm) ?? SHIPPING_TIERS.at(-1)).fee
    }
  } catch (e) {
    console.error('calculateShipping error:', e)
    shippingFee.value = -2
  } finally {
    calculatingShipping.value = false
  }
}

watch(selectedBranch, () => {
  clearTimeout(shippingTimer)
  shippingTimer = setTimeout(calculateShipping, 300)
})

watch(() => form.value.address, () => {
  clearTimeout(shippingTimer)
  shippingTimer = setTimeout(calculateShipping, 800)
})

const formatPrice = (value) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value).replace('₫', 'đ')

const buildConfirmationCode = () =>
  `KFC-${Math.random().toString(36).slice(2, 8).toUpperCase()}`

const validatePhone = (phone) => /^0[3-9]\d{8}$/.test(phone.replace(/[\s-]/g, ''))

const touchField = (field) => {
  if (field === 'fullName') {
    fieldErrors.value.fullName = form.value.fullName.trim().length < 2
      ? 'Vui lòng nhập họ và tên (ít nhất 2 ký tự).' : ''
  }
  if (field === 'phone') {
    fieldErrors.value.phone = !validatePhone(form.value.phone)
      ? 'Số điện thoại không hợp lệ (VD: 0912 345 678).' : ''
  }
  if (field === 'address') {
    fieldErrors.value.address = !form.value.address.trim()
      ? 'Vui lòng nhập địa chỉ giao hàng.' : ''
  }
}

const validateForm = () => {
  touchField('fullName')
  touchField('phone')
  touchField('address')

  const errors = [
    fieldErrors.value.fullName,
    fieldErrors.value.phone,
    fieldErrors.value.address,
    !form.value.branchId ? 'Vui lòng chọn cửa hàng.' : '',
    shippingFee.value === -1 ? 'Địa chỉ quá xa chi nhánh (tối đa 15km).' : ''
  ].filter(Boolean)

  if (errors.length > 0) {
    errors.forEach(msg => showToast(msg, 'error', 4000))
    return false
  }
  return true
}

const submitPayment = async () => {
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
      shippingFee: shippingFee.value > 0 ? shippingFee.value : 0,
      totalAmount: totalWithShipping.value
    }

    const result = await orderApi.createOrder(orderPayload)

    if (form.value.paymentMethod === PaymentMethod.VNPAY) {
      const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/payment/vnpay/create`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
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
    showToast('Đặt hàng thành công!')
  } catch (err) {
    showToast(err.message || 'Không thể tạo đơn hàng. Vui lòng thử lại.', 'error')
  }
}
</script>
