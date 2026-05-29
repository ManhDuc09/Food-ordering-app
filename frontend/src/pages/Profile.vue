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
              <!-- Payment status badge -->
              <span v-if="order.paymentStatus === 'pending' && order.paymentMethod !== 'COD'"
                class="text-xs font-semibold px-2 py-0.5 rounded-full bg-yellow-100 text-yellow-700">
                Chưa thanh toán
              </span>
              <span v-else
                class="text-xs font-semibold px-2 py-0.5 rounded-full bg-green-100 text-green-700">
                Đã thanh toán
              </span>
              <!-- Order status badge -->
              <span class="text-xs font-semibold px-2 py-0.5 rounded-full"
                :class="{
                  'bg-gray-100 text-gray-600': order.status === 'pending',
                  'bg-blue-100 text-blue-600': order.status === 'confirmed',
                  'bg-green-100 text-green-700': order.status === 'delivered',
                  'bg-red-100 text-red-600': order.status === 'cancelled'
                }">
                {{ orderStatusLabel(order.status) }}
              </span>
            </div>
          </div>

          <!-- Items -->
          <ul class="space-y-1 text-sm border-t border-gray-50 pt-3">
            <li v-for="(item, i) in order.items" :key="i" class="flex justify-between text-gray-700">
              <span>{{ item.productName }} × {{ item.quantity }}</span>
              <span class="font-medium">{{ formatPrice(item.price * item.quantity) }}</span>
            </li>
          </ul>

          <!-- Delivery + Branch info -->
          <div class="border-t border-gray-100 pt-3 space-y-1 text-xs text-gray-500">
            <div v-if="order.deliveryName || order.deliveryPhone" class="flex items-center gap-1">
              <span>👤</span>
              <span>{{ order.deliveryName }}<span v-if="order.deliveryPhone"> · {{ order.deliveryPhone }}</span></span>
            </div>
            <div v-if="order.deliveryAddress" class="flex items-start gap-1">
              <span class="shrink-0">📍</span>
              <span>{{ order.deliveryAddress }}</span>
            </div>
            <div v-if="order.branchName" class="flex items-start gap-1">
              <span class="shrink-0">🏪</span>
              <span>{{ order.branchName }}<span v-if="order.branchAddress" class="text-gray-400"> · {{ order.branchAddress }}</span></span>
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
          <div v-if="order.status !== 'cancelled' && order.status !== 'delivered'">
            <button @click="cancelOrder(order)"
              :disabled="cancellingId === order.orderId"
              class="w-full text-sm font-semibold text-gray-500 border border-gray-200 rounded-xl py-2 hover:bg-gray-50 disabled:opacity-50 transition-colors">
              {{ cancellingId === order.orderId ? 'Đang hủy...' : 'Hủy đơn hàng' }}
            </button>
          </div>

          <!-- Action buttons for pending non-COD orders -->
          <div v-if="order.status !== 'cancelled' && order.paymentStatus === 'pending' && order.paymentMethod !== 'COD'"
            class="flex gap-2">
            <button
              v-if="order.paymentMethod === 'VNPAY'"
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
      </div>

    </div>

    <!-- Address Modal -->
    <div v-if="showAddressModal" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4"
      @click.self="closeAddressModal">
      <div class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl">
        <h3 class="text-lg font-bold mb-4">{{ editingAddress ? 'Sửa địa chỉ' : 'Thêm địa chỉ' }}</h3>
        <div class="space-y-3">
          <div>
            <label class="text-sm text-gray-500 block mb-1">Địa chỉ (đường, số nhà)</label>
            <input v-model="addressForm.street" type="text" placeholder="Ví dụ: 123 Nguyễn Huệ"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
          </div>
          <div>
            <label class="text-sm text-gray-500 block mb-1">Thành phố</label>
            <input v-model="addressForm.city" type="text" placeholder="Ví dụ: Hồ Chí Minh"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400" />
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
import { ref, computed, onMounted } from 'vue'
import { profileApi } from '../api/profile'
import { showToast } from '../store/toast'

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

const editingProfile = ref(false)
const savingProfile = ref(false)
const profileError = ref('')
const profileForm = ref({ fullName: '', phoneNumber: '' })

const showAddressModal = ref(false)
const editingAddress = ref(null)
const savingAddress = ref(false)
const addressError = ref('')
const addressForm = ref({ street: '', city: '' })

const showChangeMethodModal = ref(false)
const changingOrder = ref(null)
const selectedMethod = ref('COD')
const savingMethod = ref(false)
const changeMethodError = ref('')
const repayingId = ref(null)
const cancellingId = ref(null)

const paymentMethods = [
  { value: 'COD', icon: '🛵', label: 'Thanh toán khi nhận hàng', desc: 'Trả tiền mặt khi giao hàng' },
  { value: 'MOMO', icon: '💜', label: 'MoMo', desc: 'Ví điện tử MoMo' },
  { value: 'VNPAY', icon: '🏦', label: 'VNPay', desc: 'Thanh toán qua VNPay' }
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
    orders.value = await profileApi.getMyOrders()
    ordersLoaded.value = true
  } finally {
    loadingOrders.value = false
  }
}

// Watch tab changes to load orders on demand
import { watch } from 'vue'
watch(activeTab, val => { if (val === 'orders') onOrdersTabOpen() })

// --- Profile ---
function startEditProfile() {
  profileForm.value = { fullName: profile.value.fullName || '', phoneNumber: profile.value.phoneNumber || '' }
  profileError.value = ''
  editingProfile.value = true
}

function cancelEditProfile() { editingProfile.value = false }

async function saveProfile() {
  if (!profileForm.value.fullName.trim()) { profileError.value = 'Vui lòng nhập họ và tên'; return }
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

// --- Addresses ---
function openAddAddress() {
  editingAddress.value = null
  addressForm.value = { street: '', city: '' }
  addressError.value = ''
  showAddressModal.value = true
}

function openEditAddress(addr) {
  editingAddress.value = addr
  addressForm.value = { street: addr.street, city: addr.city || '' }
  addressError.value = ''
  showAddressModal.value = true
}

function closeAddressModal() { showAddressModal.value = false }

async function saveAddress() {
  if (!addressForm.value.street.trim()) { addressError.value = 'Vui lòng nhập địa chỉ'; return }
  savingAddress.value = true
  addressError.value = ''
  try {
    if (editingAddress.value) {
      const updated = await profileApi.updateAddress(editingAddress.value.id, addressForm.value)
      const idx = addresses.value.findIndex(a => a.id === updated.id)
      if (idx !== -1) addresses.value[idx] = updated
    } else {
      addresses.value.push(await profileApi.addAddress(addressForm.value))
    }
    showAddressModal.value = false
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
    const token = sessionStorage.getItem('token')
    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/payment/vnpay/create`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
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

const paymentMethodLabel = m => ({ COD: 'Tiền mặt khi nhận', MOMO: 'MoMo', VNPAY: 'VNPay' }[m] || m)

const orderStatusLabel = s => ({ pending: 'Đang xử lý', confirmed: 'Đã xác nhận', delivered: 'Đã giao', cancelled: 'Đã hủy' }[s] || s)
</script>
