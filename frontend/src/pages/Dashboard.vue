<template>
  <div class="min-h-screen bg-gray-100 flex">
    <!-- Sidebar -->
    <aside class="w-64 bg-[#E4002B] flex flex-col shadow-xl flex-shrink-0">
      <div class="p-6 border-b border-red-700">
        <div class="flex items-center gap-3">
          <div class="bg-white p-2 rounded">
            <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" alt="KFC" class="w-10 h-10 object-contain" />
          </div>
          <div>
            <p class="text-white font-black text-lg leading-none">KFC</p>
            <p class="text-red-200 text-xs">Cổng Quản Lý</p>
          </div>
        </div>
      </div>

      <nav class="flex-1 p-4 space-y-1">
        <button
          @click="activeTab = 'orders'"
          :class="['w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-bold transition-colors text-left',
            activeTab === 'orders' ? 'bg-white text-[#E4002B]' : 'text-white hover:bg-red-700']"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          Đơn hàng
        </button>

        <button
          @click="activeTab = 'stats'"
          :class="['w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-bold transition-colors text-left',
            activeTab === 'stats' ? 'bg-white text-[#E4002B]' : 'text-white hover:bg-red-700']"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
          Thống kê
        </button>
      </nav>

      <div class="p-4 border-t border-red-700">
        <div class="mb-3 px-2">
          <p class="text-white font-bold text-sm truncate">{{ userEmail }}</p>
          <p class="text-red-200 text-xs">{{ branchName || 'Quản lý chi nhánh' }}</p>
        </div>
        <button
          @click="handleLogout"
          class="w-full flex items-center gap-2 px-4 py-2 rounded-lg text-white hover:bg-red-700 transition-colors text-sm font-bold"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
          </svg>
          Đăng xuất
        </button>
      </div>
    </aside>

    <!-- Main content -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- Topbar -->
      <header class="bg-white shadow-sm px-8 py-4 flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-black text-gray-900 uppercase">
            {{ activeTab === 'orders' ? 'Đơn hàng' : 'Thống kê' }}
          </h1>
          <p class="text-sm text-gray-500">{{ branchName }}</p>
        </div>
        <div class="flex items-center gap-3">
          <span class="text-sm text-gray-500">{{ today }}</span>
          <button
            @click="loadOrders"
            class="flex items-center gap-2 bg-[#E4002B] text-white px-4 py-2 rounded-lg text-sm font-bold hover:bg-red-700 transition-colors"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Làm mới
          </button>
        </div>
      </header>

      <main class="flex-1 overflow-y-auto p-8">
        <!-- Stats Tab -->
        <div v-if="activeTab === 'stats'" class="space-y-6">
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-[#E4002B]">
              <p class="text-sm text-gray-500 font-bold uppercase">Tổng đơn hàng</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ orders.length }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-yellow-400">
              <p class="text-sm text-gray-500 font-bold uppercase">Chờ xử lý</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ countByStatus('pending') }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-blue-500">
              <p class="text-sm text-gray-500 font-bold uppercase">Đang chuẩn bị</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ countByStatus('preparing') }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-green-500">
              <p class="text-sm text-gray-500 font-bold uppercase">Đã giao</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ countByStatus('delivered') }}</p>
            </div>
          </div>

          <div class="bg-white rounded-xl p-6 shadow-sm">
            <h2 class="font-black text-lg uppercase mb-4">Doanh thu</h2>
            <p class="text-4xl font-black text-[#E4002B]">{{ formatCurrency(totalRevenue) }}</p>
            <p class="text-sm text-gray-400 mt-1">từ {{ orders.length }} đơn hàng</p>
          </div>
        </div>

        <!-- Orders Tab -->
        <div v-if="activeTab === 'orders'">
          <!-- Filter bar -->
          <div class="flex flex-wrap gap-2 mb-6">
            <button
              v-for="f in filters"
              :key="f.value"
              @click="activeFilter = f.value"
              :class="['px-4 py-2 rounded-full text-sm font-bold border-2 transition-colors',
                activeFilter === f.value
                  ? 'bg-[#E4002B] text-white border-[#E4002B]'
                  : 'bg-white text-gray-600 border-gray-200 hover:border-[#E4002B]']"
            >
              {{ f.label }}
              <span class="ml-1 opacity-70">({{ f.value === 'all' ? orders.length : countByStatus(f.value) }})</span>
            </button>
          </div>

          <!-- Loading -->
          <div v-if="loading" class="flex justify-center py-16">
            <svg class="animate-spin w-10 h-10 text-[#E4002B]" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
            </svg>
          </div>

          <!-- Error -->
          <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-xl p-6 text-red-700 font-bold">
            {{ error }}
          </div>

          <!-- Empty -->
          <div v-else-if="filteredOrders.length === 0" class="bg-white rounded-xl p-16 text-center shadow-sm">
            <p class="text-5xl mb-4">🍗</p>
            <p class="font-bold text-gray-400">Không có đơn hàng</p>
          </div>

          <!-- Order cards -->
          <div v-else class="space-y-4">
            <div
              v-for="order in filteredOrders"
              :key="order.orderId"
              class="bg-white rounded-xl shadow-sm overflow-hidden"
            >
              <!-- Order header -->
              <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100">
                <div class="flex items-center gap-4">
                  <div>
                    <p class="font-black text-sm text-gray-900">
                      #{{ order.orderId.slice(0, 8).toUpperCase() }}
                    </p>
                    <p class="text-xs text-gray-400">{{ formatDate(order.createdAt) }}</p>
                  </div>
                  <span :class="['px-3 py-1 rounded-full text-xs font-black uppercase', statusClass(order.status)]">
                    {{ statusLabel(order.status) }}
                  </span>
                </div>
                <p class="font-black text-[#E4002B] text-lg">{{ formatCurrency(order.totalAmount) }}</p>
              </div>

              <!-- Order body -->
              <div class="px-6 py-4 grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                <div>
                  <p class="text-xs text-gray-400 uppercase font-bold mb-1">Khách hàng</p>
                  <p class="font-bold text-gray-800">{{ order.deliveryName }}</p>
                  <p class="text-gray-500">{{ order.customerEmail }}</p>
                  <p class="text-gray-500">{{ order.deliveryPhone }}</p>
                </div>
                <div>
                  <p class="text-xs text-gray-400 uppercase font-bold mb-1">Địa chỉ giao hàng</p>
                  <p class="text-gray-700">{{ order.deliveryAddress || '—' }}</p>
                </div>
                <div>
                  <p class="text-xs text-gray-400 uppercase font-bold mb-1">Thanh toán</p>
                  <p class="font-bold text-gray-800">{{ order.paymentMethod }}</p>
                  <span v-if="order.paymentMethod !== 'COD' || order.paymentStatus === 'paid'"
                    :class="['text-xs font-bold', order.paymentStatus === 'paid' ? 'text-green-600' : 'text-yellow-600']">
                    {{ order.paymentStatus === 'paid' ? 'Đã thanh toán' : 'Chưa thanh toán' }}
                  </span>
                </div>
              </div>

              <!-- Items -->
              <div class="px-6 pb-3">
                <p class="text-xs text-gray-400 uppercase font-bold mb-2">Sản phẩm</p>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="item in order.items"
                    :key="item.productName"
                    class="bg-gray-100 text-gray-700 text-xs font-bold px-3 py-1 rounded-full"
                  >
                    {{ item.quantity }}x {{ item.productName }}
                  </span>
                </div>
              </div>

              <!-- Actions -->
              <div class="px-6 py-4 bg-gray-50 flex flex-wrap gap-2">
                <button
                  v-for="action in statusActions(order.status)"
                  :key="action.status"
                  @click="changeStatus(order.orderId, action.status)"
                  :disabled="updatingId === order.orderId"
                  :class="['px-4 py-2 rounded-lg text-xs font-black uppercase transition-colors disabled:opacity-50', action.class]"
                >
                  {{ action.label }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { managerApi } from '../api/manager'
import { logout } from '../store/auth'
import { showToast } from '../store/toast'

const router = useRouter()

const user = JSON.parse(sessionStorage.getItem('user') || 'null')
const userEmail = user?.email || ''
const branchName = user?.branchName || ''

const orders = ref([])
const loading = ref(false)
const error = ref('')
const activeTab = ref('orders')
const activeFilter = ref('all')
const updatingId = ref(null)

const today = new Date().toLocaleDateString('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })

const filters = [
  { label: 'Tất cả', value: 'all' },
  { label: 'Chờ xử lý', value: 'pending' },
  { label: 'Đang chuẩn bị', value: 'preparing' },
  { label: 'Đang giao', value: 'delivering' },
  { label: 'Đã giao', value: 'delivered' },
  { label: 'Đã hủy', value: 'cancelled' },
]

const filteredOrders = computed(() =>
  activeFilter.value === 'all'
    ? orders.value
    : orders.value.filter(o => o.status === activeFilter.value)
)

const totalRevenue = computed(() =>
  orders.value.reduce((sum, o) => sum + Number(o.totalAmount), 0)
)

const countByStatus = (status) => orders.value.filter(o => o.status === status).length

const loadOrders = async () => {
  loading.value = true
  error.value = ''
  try {
    orders.value = await managerApi.getOrders()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

const changeStatus = async (orderId, status) => {
  updatingId.value = orderId
  try {
    const updated = await managerApi.updateStatus(orderId, status)
    const idx = orders.value.findIndex(o => o.orderId === orderId)
    if (idx !== -1) orders.value[idx] = updated
  } catch (e) {
    alert(e.message)
  } finally {
    updatingId.value = null
  }
}

const handleLogout = async () => {
  await logout()
  showToast('Đã đăng xuất.', 'error')
  router.push('/login')
}

const statusActions = (status) => {
  const map = {
    pending:    [{ label: 'Bắt đầu chuẩn bị', status: 'preparing', class: 'bg-blue-600 text-white hover:bg-blue-700' },
                 { label: 'Hủy đơn', status: 'cancelled', class: 'bg-gray-200 text-gray-700 hover:bg-gray-300' }],
    preparing:  [{ label: 'Đang giao hàng', status: 'delivering', class: 'bg-yellow-500 text-white hover:bg-yellow-600' }],
    delivering: [{ label: 'Xác nhận đã giao', status: 'delivered', class: 'bg-green-600 text-white hover:bg-green-700' }],
    delivered:  [],
    cancelled:  [],
  }
  return map[status] || []
}

const statusLabel = (status) => ({
  pending: 'Chờ xử lý',
  preparing: 'Đang chuẩn bị',
  delivering: 'Đang giao',
  delivered: 'Đã giao',
  cancelled: 'Đã hủy',
}[status] || status)

const statusClass = (status) => {
  const map = {
    pending:    'bg-yellow-100 text-yellow-800',
    preparing:  'bg-blue-100 text-blue-800',
    delivering: 'bg-orange-100 text-orange-800',
    delivered:  'bg-green-100 text-green-800',
    cancelled:  'bg-gray-100 text-gray-600',
  }
  return map[status] || 'bg-gray-100 text-gray-600'
}

const formatCurrency = (amount) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount)

const formatDate = (dateStr) => {
  if (!dateStr) return '—'
  return new Date(dateStr).toLocaleString('vi-VN')
}

onMounted(loadOrders)
</script>
