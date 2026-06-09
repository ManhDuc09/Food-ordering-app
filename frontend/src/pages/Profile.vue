<template>
  <div class="min-h-screen bg-gray-50 py-10 px-4">
    <div class="max-w-2xl mx-auto space-y-6">

      <!-- Header -->
      <div class="flex items-center gap-3">
        <div class="bg-red-600 text-white rounded-full w-12 h-12 flex items-center justify-center text-xl font-bold">
          {{ initials }}
        </div>
        <div>
          <h1 class="text-2xl font-bold">Tài khoản của tôi</h1>
          <p class="text-sm text-gray-500">{{ profile.email }}</p>
        </div>
      </div>

      <!-- Tabs -->
      <div class="flex border-b border-gray-200">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          @click="activeTab = tab.key"
          class="px-5 py-3 text-sm font-semibold border-b-2 transition-colors"
          :class="activeTab === tab.key
            ? 'border-red-600 text-red-600'
            : 'border-transparent text-gray-500 hover:text-gray-700'"
        >{{ tab.label }}</button>
      </div>

      <!-- Tab: Personal Info -->
      <div v-if="activeTab === 'info'" class="bg-white rounded-2xl shadow-sm p-6">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold">Thông tin cá nhân</h2>
          <button
            v-if="!editingProfile"
            @click="startEditProfile"
            class="text-sm text-red-600 font-semibold hover:underline"
          >Chỉnh sửa</button>
        </div>

        <div v-if="!editingProfile" class="space-y-3 text-sm">
          <div class="flex justify-between">
            <span class="text-gray-500">Họ và tên</span>
            <span class="font-medium">{{ profile.fullName || '—' }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-500">Email</span>
            <span class="font-medium">{{ profile.email }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-500">Số điện thoại</span>
            <span class="font-medium">{{ profile.phoneNumber || '—' }}</span>
          </div>
        </div>

        <div v-else class="space-y-3">
          <div>
            <label class="text-sm text-gray-500 block mb-1">Họ và tên</label>
            <input v-model="profileForm.fullName" type="text"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
          </div>
          <div>
            <label class="text-sm text-gray-500 block mb-1">Số điện thoại</label>
            <input v-model="profileForm.phoneNumber" type="text"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
          </div>
          <p v-if="profileError" class="text-red-500 text-xs">{{ profileError }}</p>
          <div class="flex gap-2 pt-1">
            <button @click="saveProfile" :disabled="savingProfile"
              class="bg-red-600 text-white text-sm font-semibold px-4 py-2 rounded-lg hover:bg-red-700 disabled:opacity-50">
              {{ savingProfile ? 'Đang lưu...' : 'Lưu' }}
            </button>
            <button @click="cancelEditProfile"
              class="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100">Hủy</button>
          </div>
        </div>
      </div>

      <!-- Password change card -->
      <div v-if="activeTab === 'info'" class="bg-white rounded-2xl shadow-sm p-6">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold">Đổi mật khẩu</h2>
          <button v-if="!changingPassword" @click="changingPassword = true"
            class="text-sm text-red-600 font-semibold hover:underline">Thay đổi</button>
        </div>

        <div v-if="!changingPassword" class="text-sm text-gray-400">••••••••</div>

        <div v-else class="space-y-3">
          <div>
            <label class="text-sm text-gray-500 block mb-1">Mật khẩu hiện tại</label>
            <input v-model="passwordForm.current" type="password"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
          </div>
          <div>
            <label class="text-sm text-gray-500 block mb-1">Mật khẩu mới</label>
            <input v-model="passwordForm.next" type="password"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
          </div>
          <div>
            <label class="text-sm text-gray-500 block mb-1">Xác nhận mật khẩu mới</label>
            <input v-model="passwordForm.confirm" type="password"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
          </div>
          <div class="flex gap-2 pt-1">
            <button @click="submitPasswordChange" :disabled="savingPassword"
              class="bg-red-600 text-white text-sm font-semibold px-4 py-2 rounded-lg hover:bg-red-700 disabled:opacity-50">
              {{ savingPassword ? 'Đang lưu...' : 'Lưu' }}
            </button>
            <button @click="changingPassword = false"
              class="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100">Hủy</button>
          </div>
        </div>
      </div>

      <!-- Tab: Addresses -->
      <div v-if="activeTab === 'addresses'" class="bg-white rounded-2xl shadow-sm p-6">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold">Địa chỉ giao hàng</h2>
          <button @click="openAddAddress" class="text-sm text-red-600 font-semibold hover:underline">
            + Thêm địa chỉ
          </button>
        </div>

        <div v-if="addresses.length === 0" class="text-sm text-gray-400 text-center py-6">
          Bạn chưa có địa chỉ nào.
        </div>

        <ul class="space-y-3">
          <li v-for="addr in addresses" :key="addr.id"
            class="border rounded-xl p-4 text-sm"
            :class="addr.isDefault ? 'border-red-300 bg-red-50' : 'border-gray-100'">
            <div class="flex items-start justify-between gap-2">
              <div>
                <p class="font-medium">{{ addr.street }}</p>
                <p class="text-gray-500">{{ addr.city }}</p>
                <span v-if="addr.isDefault"
                  class="inline-block mt-1 text-xs text-red-600 font-semibold bg-red-100 px-2 py-0.5 rounded-full">
                  Mặc định
                </span>
              </div>
              <div class="flex flex-col gap-1 items-end shrink-0">
                <button @click="openEditAddress(addr)" class="text-xs text-blue-500 hover:underline">Sửa</button>
                <button @click="removeAddress(addr.id)" class="text-xs text-red-400 hover:underline">Xóa</button>
                <button v-if="!addr.isDefault" @click="makeDefault(addr.id)"
                  class="text-xs text-gray-500 hover:underline">Đặt mặc định</button>
              </div>
            </div>
          </li>
        </ul>
      </div>

      <!-- Tab: Orders -->
      <div v-if="activeTab === 'orders'" class="space-y-4">
        <div v-if="loadingOrders" class="text-center py-10 text-gray-400 text-sm">Đang tải...</div>

        <div v-else-if="orders.length === 0" class="bg-white rounded-2xl shadow-sm p-8 text-center text-gray-400 text-sm">
          Bạn chưa có đơn hàng nào.
        </div>

        <div v-for="order in orders" :key="order.orderId"
          class="bg-white rounded-2xl shadow-sm p-5 space-y-3">

          <!-- Order header -->
          <div class="flex items-start justify-between gap-2 flex-wrap">
            <div>
              <p class="text-xs text-gray-400">{{ formatDate(order.createdAt) }}</p>
              <p class="text-xs text-gray-400 font-mono">{{ order.orderId.slice(0, 8).toUpperCase() }}</p>
            </div>
            <div class="flex items-center gap-2 flex-wrap justify-end">
              <span v-if="order.paymentStatus === PaymentStatus.PENDING"
                class="text-xs font-semibold px-2 py-0.5 rounded-full bg-yellow-100 text-yellow-700">
                Chưa thanh toán
              </span>
              <span v-else
                class="text-xs font-semibold px-2 py-0.5 rounded-full bg-green-100 text-green-700">
                Đã thanh toán
              </span>
              <span class="text-xs font-semibold px-2 py-0.5 rounded-full"
                :class="{
                  'bg-gray-100 text-gray-600': order.status === OrderStatus.PENDING,
                  'bg-blue-100 text-blue-600': order.status === OrderStatus.CONFIRMED,
                  'bg-green-100 text-green-700': order.status === OrderStatus.DELIVERED,
                  'bg-red-100 text-red-600': order.status === OrderStatus.CANCELLED,
                }">
                {{ orderStatusLabel(order.status) }}
              </span>
            </div>
          </div>

          <!-- Items -->
          <ul class="space-y-1 text-sm border-t border-gray-50 pt-3">
            <li v-for="(item, i) in order.items" :key="i" class="text-gray-700">
              {{ item.productName }} × {{ item.quantity }}
            </li>
          </ul>

          <!-- Delivery + Branch info -->
          <div class="border-t border-gray-100 pt-3 space-y-1 text-xs text-gray-500">
            <div v-if="order.deliveryName || order.deliveryPhone">
              {{ order.deliveryName }}<span v-if="order.deliveryPhone"> · {{ order.deliveryPhone }}</span>
            </div>
            <div v-if="order.deliveryAddress">{{ order.deliveryAddress }}</div>
            <div v-if="order.branchName">
              {{ order.branchName }}<span v-if="order.branchAddress" class="text-gray-400"> · {{ order.branchAddress }}</span>
            </div>
          </div>

          <!-- Total + payment method -->
          <div class="border-t border-gray-100 pt-3 flex items-center justify-between">
            <span class="text-sm text-gray-500">
              {{ paymentMethodLabel(order.paymentMethod) }}
            </span>
            <span class="font-bold text-base">{{ formatPrice(order.totalAmount) }}</span>
          </div>

          <!-- Cancel button -->
          <div v-if="order.status !== OrderStatus.CANCELLED && order.status !== OrderStatus.DELIVERED">
            <button @click="cancelOrder(order)"
              :disabled="cancellingId === order.orderId"
              class="w-full text-sm font-semibold text-gray-500 border border-gray-200 rounded-xl py-2 hover:bg-gray-50 disabled:opacity-50 transition-colors">
              {{ cancellingId === order.orderId ? 'Đang hủy...' : 'Hủy đơn hàng' }}
            </button>
          </div>

          <!-- Action buttons for pending non-COD orders -->
          <div v-if="order.status !== OrderStatus.CANCELLED && order.paymentStatus === PaymentStatus.PENDING && order.paymentMethod !== PaymentMethod.COD"
            class="flex gap-2">
            <button
              v-if="order.paymentMethod === PaymentMethod.VNPAY"
              @click="repay(order)"
              :disabled="repayingId === order.orderId"
              class="flex-1 text-sm font-semibold text-white bg-[#E4002B] rounded-xl py-2 hover:bg-red-700 disabled:opacity-50 transition-colors">
              {{ repayingId === order.orderId ? 'Đang chuyển...' : 'Thanh toán lại' }}
            </button>
            <button @click="openChangeMethod(order)"
              class="flex-1 text-sm font-semibold text-red-600 border border-red-200 rounded-xl py-2 hover:bg-red-50 transition-colors">
              Đổi phương thức
            </button>
          </div>
        </div>

        <!-- Load more button -->
        <div v-if="hasMore" class="text-center">
          <button @click="loadMore" :disabled="loadingMore"
            class="px-6 py-2 text-sm font-bold text-red-600 border border-red-200 rounded-full hover:bg-red-50 disabled:opacity-50 transition-colors">
            {{ loadingMore ? 'Đang tải...' : 'Xem thêm' }}
          </button>
        </div>
      </div>

    </div>

    <!-- Address Modal -->
    <div v-if="showAddressModal" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4"
      @click.self="closeAddressModal">
      <div class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl">
        <h3 class="text-lg font-bold mb-4">{{ editingAddress ? 'Sửa địa chỉ' : 'Thêm địa chỉ' }}</h3>
        <div class="space-y-3">

          <!-- Map search + locate -->
          <div>
            <div class="flex items-center justify-between mb-1">
              <label class="text-sm text-gray-500">Tìm trên bản đồ</label>
              <button type="button" @click="locateMe"
                class="text-xs text-red-600 font-semibold hover:underline flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a2 2 0 01-2.828 0l-4.243-4.243a8 8 0 1111.314 0z"/>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                </svg>
                Vị trí của tôi
              </button>
            </div>

            <!-- Search box -->
            <div class="relative mb-2">
              <input
                v-model="mapSearch"
                type="text"
                placeholder="Tìm địa chỉ..."
                autocomplete="off"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400 pr-8"
                @input="onMapSearchInput"
                @blur="hideMapDropdownDelayed"
              />
              <svg v-if="!mapSearching" class="absolute right-2.5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none"
                fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35M17 11A6 6 0 115 11a6 6 0 0112 0z" />
              </svg>
              <svg v-else class="absolute right-2.5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 animate-spin"
                fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"/>
              </svg>
              <div v-if="mapSuggestions.length > 0"
                class="absolute z-20 top-full left-0 right-0 bg-white border border-gray-200 rounded-lg shadow-lg mt-1 max-h-44 overflow-y-auto">
                <button
                  v-for="(s, i) in mapSuggestions" :key="i"
                  type="button"
                  @mousedown.prevent="selectMapSuggestion(s)"
                  class="w-full text-left px-3 py-2 text-xs hover:bg-red-50 border-b border-gray-100 last:border-0 text-gray-700">
                  {{ s.display_name }}
                </button>
              </div>
            </div>

            <div ref="addressMapEl" class="w-full h-44 rounded-xl border border-gray-200 overflow-hidden"></div>
            <p class="text-xs text-gray-400 mt-1 text-center">
              <span v-if="geocoding">Đang tìm địa chỉ...</span>
              <span v-else>Nhấn vào bản đồ để tự động điền địa chỉ</span>
            </p>
          </div>

          <!-- Address fields -->
          <div class="grid grid-cols-2 gap-2">
            <div>
              <label class="text-sm text-gray-500 block mb-1">Số nhà</label>
              <input v-model="addressForm.houseNumber" type="text" placeholder="VD: 12B"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
            </div>
            <div>
              <label class="text-sm text-gray-500 block mb-1">Tên đường</label>
              <input v-model="addressForm.road" type="text" placeholder="VD: Nguyễn Huệ"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
            </div>
          </div>
          <div class="grid grid-cols-2 gap-2">
            <div>
              <label class="text-sm text-gray-500 block mb-1">Phường / Xã</label>
              <input v-model="addressForm.ward" type="text" placeholder="VD: Phường Bến Nghé"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
            </div>
            <div>
              <label class="text-sm text-gray-500 block mb-1">Quận / Huyện</label>
              <input v-model="addressForm.district" type="text" placeholder="VD: Quận 1"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
            </div>
          </div>
          <div class="grid grid-cols-2 gap-2">
            <div>
              <label class="text-sm text-gray-500 block mb-1">Tỉnh / Thành phố</label>
              <input v-model="addressForm.city" type="text" placeholder="VD: Hồ Chí Minh"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
            </div>
            <div>
              <label class="text-sm text-gray-500 block mb-1">Chi tiết <span class="text-gray-400 font-normal">(tuỳ chọn)</span></label>
              <input v-model="addressForm.detail" type="text" placeholder="VD: Tầng 5, Toà A"
                class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
            </div>
          </div>

          <p v-if="addressError" class="text-red-500 text-xs">{{ addressError }}</p>
          <div class="flex gap-2 pt-1">
            <button @click="saveAddress" :disabled="savingAddress"
              class="bg-red-600 text-white text-sm font-semibold px-4 py-2 rounded-lg hover:bg-red-700 disabled:opacity-50">
              {{ savingAddress ? 'Đang lưu...' : 'Lưu' }}
            </button>
            <button @click="closeAddressModal"
              class="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100">Hủy</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Change Payment Method Modal -->
    <div v-if="showChangeMethodModal" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4"
      @click.self="closeChangeMethod">
      <div class="bg-white rounded-2xl p-6 w-full max-w-sm shadow-xl">
        <h3 class="text-lg font-bold mb-4">Đổi phương thức thanh toán</h3>
        <div class="space-y-3 mb-5">
          <label v-for="m in paymentMethods" :key="m.value"
            class="flex items-center gap-3 cursor-pointer rounded-xl border-2 px-4 py-3 transition-colors"
            :class="selectedMethod === m.value ? 'border-red-500 bg-red-50' : 'border-gray-100'">
            <input type="radio" :value="m.value" v-model="selectedMethod" class="h-4 w-4 accent-red-600" />
            <span class="text-xl">{{ m.icon }}</span>
            <div>
              <p class="font-semibold text-sm">{{ m.label }}</p>
              <p class="text-xs text-gray-400">{{ m.desc }}</p>
            </div>
          </label>
        </div>
        <p v-if="changeMethodError" class="text-red-500 text-xs mb-3">{{ changeMethodError }}</p>
        <div class="flex gap-2">
          <button @click="saveNewMethod" :disabled="savingMethod"
            class="flex-1 bg-red-600 text-white text-sm font-semibold py-2 rounded-xl hover:bg-red-700 disabled:opacity-50">
            {{ savingMethod ? 'Đang lưu...' : 'Xác nhận' }}
          </button>
          <button @click="closeChangeMethod"
            class="flex-1 text-sm text-gray-500 py-2 rounded-xl hover:bg-gray-100">Hủy</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { profileApi } from '../api/profile'
import { showToast } from '../store/toast'
import { OrderStatus, PaymentStatus, PaymentMethod } from '../constants/enums'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

const tabs = [
  { key: 'info', label: 'Thông tin cá nhân' },
  { key: 'addresses', label: 'Địa chỉ' },
  { key: 'orders', label: 'Đơn hàng' }
]
const activeTab = ref('info')

const profile = ref({ fullName: '', email: '', phoneNumber: '' })
const addresses = ref([])
const orders = ref([])
const loadingOrders = ref(false)
const loadingMore = ref(false)
const hasMore = ref(false)
const currentPage = ref(0)

const editingProfile = ref(false)
const savingProfile = ref(false)
const profileError = ref('')
const profileForm = ref({ fullName: '', phoneNumber: '' })

const showAddressModal = ref(false)
const editingAddress = ref(null)
const savingAddress = ref(false)
const addressError = ref('')
const addressMapEl = ref(null)
const addressMapInstance = ref(null)
const addressMarker = ref(null)
const geocoding = ref(false)
const addressForm = ref({ houseNumber: '', road: '', ward: '', district: '', detail: '', city: '', latitude: null, longitude: null })

// Map search
const mapSearch = ref('')
const mapSuggestions = ref([])
const mapSearching = ref(false)

const showChangeMethodModal = ref(false)
const changingOrder = ref(null)
const selectedMethod = ref('COD')
const savingMethod = ref(false)
const changeMethodError = ref('')
const repayingId = ref(null)
const cancellingId = ref(null)
const changingPassword = ref(false)
const savingPassword = ref(false)
const passwordForm = ref({ current: '', next: '', confirm: '' })

const paymentMethods = [
  { value: PaymentMethod.COD,   icon: '🛵', label: 'Thanh toán khi nhận hàng', desc: 'Trả tiền mặt khi giao hàng' },
  { value: PaymentMethod.MOMO,  icon: '💜', label: 'MoMo', desc: 'Ví điện tử MoMo' },
  { value: PaymentMethod.VNPAY, icon: '🏦', label: 'VNPay', desc: 'Thanh toán qua VNPay' },
]

const initials = computed(() => {
  const name = profile.value.fullName || profile.value.email || '?'
  return name.charAt(0).toUpperCase()
})

onMounted(async () => {
  const [prof, addrs] = await Promise.all([
    profileApi.getProfile(),
    profileApi.getAddresses()
  ])
  profile.value = prof
  addresses.value = addrs
})

// Load orders lazily when tab is first opened
const ordersLoaded = ref(false)
async function onOrdersTabOpen() {
  if (ordersLoaded.value) return
  loadingOrders.value = true
  try {
    const data = await profileApi.getMyOrders(0)
    orders.value = data.orders
    hasMore.value = data.hasMore
    currentPage.value = 0
    ordersLoaded.value = true
  } finally {
    loadingOrders.value = false
  }
}

async function loadMore() {
  loadingMore.value = true
  try {
    const nextPage = currentPage.value + 1
    const data = await profileApi.getMyOrders(nextPage)
    orders.value.push(...data.orders)
    hasMore.value = data.hasMore
    currentPage.value = nextPage
  } finally {
    loadingMore.value = false
  }
}

import { watch } from 'vue'
watch(activeTab, val => { if (val === 'orders') onOrdersTabOpen() })

// Forward geocode when address fields are typed manually
let fieldGeoTimer = null
watch(
  [
    () => addressForm.value.houseNumber,
    () => addressForm.value.road,
    () => addressForm.value.ward,
    () => addressForm.value.district,
    () => addressForm.value.city,
  ],
  () => {
    clearTimeout(fieldGeoTimer)
    const road = [addressForm.value.houseNumber, addressForm.value.road].filter(Boolean).join(' ')
    if (!road.trim()) return
    fieldGeoTimer = setTimeout(() => forwardGeocode(), 900)
  }
)

let lastGeoQuery = ''
async function forwardGeocode() {
  const parts = [
    [addressForm.value.houseNumber, addressForm.value.road].map(s => s?.trim()).filter(Boolean).join(' '),
    addressForm.value.ward?.trim(),
    addressForm.value.district?.trim(),
    addressForm.value.city?.trim(),
    'Việt Nam',
  ].filter(Boolean)

  if (!parts[0]) return
  const q = parts.join(', ')
  if (q === lastGeoQuery) return
  lastGeoQuery = q

  try {
    const res = await fetch(
      `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(q)}&format=json&limit=1`,
      { headers: { 'Accept-Language': 'vi' } }
    )
    const data = await res.json()
    if (!data.length) return
    const lat = parseFloat(data[0].lat)
    const lng = parseFloat(data[0].lon)
    addressForm.value.latitude = lat
    addressForm.value.longitude = lng
    const map = addressMapInstance.value
    if (map) {
      map.setView([lat, lng], 16)
      placeMarker(map, lat, lng)
    }
  } catch {}
}

// --- Profile ---
function startEditProfile() {
  profileForm.value = { fullName: profile.value.fullName || '', phoneNumber: profile.value.phoneNumber || '' }
  profileError.value = ''
  editingProfile.value = true
}

function cancelEditProfile() { editingProfile.value = false }

async function saveProfile() {
  if (!profileForm.value.fullName.trim()) {
    showToast('Vui lòng nhập họ và tên.', 'error'); return
  }
  if (profileForm.value.phoneNumber && !/^0[3-9]\d{8}$/.test(profileForm.value.phoneNumber.replace(/[\s-]/g, ''))) {
    showToast('Số điện thoại không hợp lệ (VD: 0912 345 678).', 'error'); return
  }
  savingProfile.value = true
  profileError.value = ''
  try {
    profile.value = await profileApi.updateProfile(profileForm.value)
    editingProfile.value = false
    showToast('Cập nhật thông tin thành công!')
  } catch {
    profileError.value = 'Cập nhật thất bại, thử lại sau'
    showToast('Cập nhật thất bại, thử lại sau', 'error')
  } finally {
    savingProfile.value = false
  }
}

// --- Map search ---
let mapSearchTimer = null

function onMapSearchInput() {
  clearTimeout(mapSearchTimer)
  if (!mapSearch.value.trim() || mapSearch.value.length < 3) {
    mapSuggestions.value = []
    return
  }
  mapSearchTimer = setTimeout(fetchMapSuggestions, 400)
}

async function fetchMapSuggestions() {
  mapSearching.value = true
  try {
    const res = await fetch(
      `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(mapSearch.value + ', Việt Nam')}&format=json&limit=5&addressdetails=1`,
      { headers: { 'Accept-Language': 'vi' } }
    )
    mapSuggestions.value = await res.json()
  } catch {
    mapSuggestions.value = []
  } finally {
    mapSearching.value = false
  }
}

function selectMapSuggestion(s) {
  const lat = parseFloat(s.lat)
  const lng = parseFloat(s.lon)
  mapSuggestions.value = []
  mapSearch.value = ''

  const map = addressMapInstance.value
  if (map) {
    map.setView([lat, lng], 16)
    placeMarker(map, lat, lng)
  }
  addressForm.value.latitude = lat
  addressForm.value.longitude = lng
  fillAddressFromNominatim(s.address || {})
}

function hideMapDropdownDelayed() {
  setTimeout(() => { mapSuggestions.value = [] }, 150)
}

// --- Address map ---
async function initAddressMap() {
  await nextTick()
  if (!addressMapEl.value) return

  const map = L.map(addressMapEl.value).setView([10.7626, 106.6602], 13)
  addressMapInstance.value = map

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap'
  }).addTo(map)

  map.on('click', async ({ latlng }) => {
    placeMarker(map, latlng.lat, latlng.lng)
    addressForm.value.latitude = latlng.lat
    addressForm.value.longitude = latlng.lng
    await reverseGeocode(latlng.lat, latlng.lng)
  })
}

function placeMarker(map, lat, lng) {
  if (addressMarker.value) {
    addressMarker.value.setLatLng([lat, lng])
  } else {
    addressMarker.value = L.marker([lat, lng]).addTo(map)
  }
}

function fillAddressFromNominatim(a) {
  if (a.house_number) addressForm.value.houseNumber = a.house_number
  const road = a.road || a.pedestrian || a.path || ''
  if (road) addressForm.value.road = road
  const ward = a.quarter || a.suburb || a.neighbourhood || ''
  if (ward) addressForm.value.ward = ward
  const district = a.city_district || a.county || ''
  if (district) addressForm.value.district = district
  const city = a.city || a.town || a.village || a.state || ''
  if (city) addressForm.value.city = city
}

async function reverseGeocode(lat, lng) {
  geocoding.value = true
  try {
    const res = await fetch(
      `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json`,
      { headers: { 'Accept-Language': 'vi' } }
    )
    const data = await res.json()
    fillAddressFromNominatim(data.address || {})
  } catch {}
  finally { geocoding.value = false }
}

function locateMe() {
  if (!navigator.geolocation) { showToast('Trình duyệt không hỗ trợ định vị.', 'error'); return }
  navigator.geolocation.getCurrentPosition(
    async ({ coords }) => {
      const { latitude: lat, longitude: lng } = coords
      const map = addressMapInstance.value
      if (map) {
        map.setView([lat, lng], 16)
        placeMarker(map, lat, lng)
      }
      addressForm.value.latitude = lat
      addressForm.value.longitude = lng
      await reverseGeocode(lat, lng)
    },
    () => showToast('Không thể lấy vị trí của bạn.', 'error')
  )
}

function buildStreet() {
  return [addressForm.value.houseNumber, addressForm.value.road, addressForm.value.detail]
    .map(s => s?.trim()).filter(Boolean).join(', ')
}

function buildCity() {
  return [addressForm.value.ward, addressForm.value.district, addressForm.value.city]
    .map(s => s?.trim()).filter(Boolean).join(', ')
}

async function openAddAddress() {
  editingAddress.value = null
  addressForm.value = { houseNumber: '', road: '', ward: '', district: '', detail: '', city: '', latitude: null, longitude: null }
  mapSearch.value = ''
  mapSuggestions.value = []
  addressError.value = ''
  addressMarker.value = null
  lastGeoQuery = ''
  showAddressModal.value = true
  initAddressMap()
}

async function openEditAddress(addr) {
  editingAddress.value = addr
  addressForm.value = {
    houseNumber: '',
    road: addr.street || '',
    ward: '',
    district: '',
    detail: '',
    city: addr.city || '',
    latitude: addr.latitude || null,
    longitude: addr.longitude || null
  }
  mapSearch.value = ''
  mapSuggestions.value = []
  addressError.value = ''
  addressMarker.value = null
  lastGeoQuery = ''
  showAddressModal.value = true
  initAddressMap()
}

function closeAddressModal() {
  if (addressMapInstance.value) {
    addressMapInstance.value.remove()
    addressMapInstance.value = null
  }
  mapSuggestions.value = []
  showAddressModal.value = false
}

async function saveAddress() {
  const street = buildStreet()
  if (!street) {
    showToast('Vui lòng nhập ít nhất số nhà hoặc tên đường.', 'error'); return
  }
  savingAddress.value = true
  addressError.value = ''
  try {
    const payload = {
      street: buildStreet(),
      city: buildCity(),
      latitude: addressForm.value.latitude,
      longitude: addressForm.value.longitude
    }
    if (editingAddress.value) {
      const updated = await profileApi.updateAddress(editingAddress.value.id, payload)
      const idx = addresses.value.findIndex(a => a.id === updated.id)
      if (idx !== -1) addresses.value[idx] = updated
    } else {
      addresses.value.push(await profileApi.addAddress(payload))
    }
    closeAddressModal()
    showToast(editingAddress.value ? 'Cập nhật địa chỉ thành công!' : 'Thêm địa chỉ thành công!')
  } catch {
    addressError.value = 'Lưu địa chỉ thất bại, thử lại sau'
    showToast('Lưu địa chỉ thất bại, thử lại sau', 'error')
  } finally {
    savingAddress.value = false
  }
}

async function removeAddress(id) {
  try {
    await profileApi.deleteAddress(id)
    addresses.value = addresses.value.filter(a => a.id !== id)
    showToast('Đã xóa địa chỉ.')
  } catch {
    showToast('Xóa địa chỉ thất bại', 'error')
  }
}

async function makeDefault(id) {
  const updated = await profileApi.setDefaultAddress(id)
  addresses.value = addresses.value.map(a => ({ ...a, isDefault: a.id === updated.id }))
}

// --- Password change ---
async function submitPasswordChange() {
  if (!passwordForm.value.current) {
    showToast('Vui lòng nhập mật khẩu hiện tại.', 'error'); return
  }
  if (passwordForm.value.next.length < 6) {
    showToast('Mật khẩu mới phải có ít nhất 6 ký tự.', 'error'); return
  }
  if (passwordForm.value.next !== passwordForm.value.confirm) {
    showToast('Mật khẩu xác nhận không khớp.', 'error'); return
  }
  savingPassword.value = true
  try {
    await profileApi.changePassword(passwordForm.value.current, passwordForm.value.next)
    showToast('Đổi mật khẩu thành công!')
    changingPassword.value = false
    passwordForm.value = { current: '', next: '', confirm: '' }
  } catch (e) {
    showToast(e.message || 'Đổi mật khẩu thất bại.', 'error')
  } finally {
    savingPassword.value = false
  }
}

// --- Cancel order ---
async function cancelOrder(order) {
  if (!confirm('Bạn có chắc muốn hủy đơn hàng này?')) return
  cancellingId.value = order.orderId
  try {
    const updated = await profileApi.cancelOrder(order.orderId)
    const idx = orders.value.findIndex(o => o.orderId === updated.orderId)
    if (idx !== -1) orders.value[idx] = updated
    showToast('Đã hủy đơn hàng.')
  } catch (e) {
    showToast(e.message || 'Hủy đơn thất bại', 'error')
  } finally {
    cancellingId.value = null
  }
}

// --- Repay via VNPay ---
async function repay(order) {
  repayingId.value = order.orderId
  try {
    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/payment/vnpay/create`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ orderId: order.orderId })
    })
    if (!res.ok) throw new Error()
    const { paymentUrl } = await res.json()
    window.location.href = paymentUrl
  } catch {
    alert('Không thể tạo liên kết thanh toán. Vui lòng thử lại.')
    repayingId.value = null
  }
}

// --- Change payment method ---
function openChangeMethod(order) {
  changingOrder.value = order
  selectedMethod.value = order.paymentMethod || 'COD'
  changeMethodError.value = ''
  showChangeMethodModal.value = true
}

function closeChangeMethod() { showChangeMethodModal.value = false }

async function saveNewMethod() {
  savingMethod.value = true
  changeMethodError.value = ''
  try {
    const updated = await profileApi.updatePaymentMethod(changingOrder.value.orderId, selectedMethod.value)
    const idx = orders.value.findIndex(o => o.orderId === updated.orderId)
    if (idx !== -1) orders.value[idx] = updated
    showChangeMethodModal.value = false
    showToast('Đã cập nhật phương thức thanh toán!')
  } catch {
    changeMethodError.value = 'Cập nhật thất bại, thử lại sau'
    showToast('Cập nhật thất bại, thử lại sau', 'error')
  } finally {
    savingMethod.value = false
  }
}

// --- Helpers ---
const formatPrice = val =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val).replace('₫', 'đ')

const formatDate = iso => iso
  ? new Intl.DateTimeFormat('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' }).format(new Date(iso))
  : '—'

const paymentMethodLabel = m => ({
  [PaymentMethod.COD]:   'Tiền mặt khi nhận',
  [PaymentMethod.MOMO]:  'MoMo',
  [PaymentMethod.VNPAY]: 'VNPay',
}[m] || m)

const orderStatusLabel = s => ({
  [OrderStatus.PENDING]:   'Đang xử lý',
  [OrderStatus.CONFIRMED]: 'Đã xác nhận',
  [OrderStatus.DELIVERED]: 'Đã giao',
  [OrderStatus.CANCELLED]: 'Đã hủy',
}[s] || s)
</script>
