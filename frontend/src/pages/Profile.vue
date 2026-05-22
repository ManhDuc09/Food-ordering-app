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

      <!-- Personal Info Card -->
      <div class="bg-white rounded-2xl shadow-sm p-6">
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
            <input
              v-model="profileForm.fullName"
              type="text"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400"
            />
          </div>
          <div>
            <label class="text-sm text-gray-500 block mb-1">Số điện thoại</label>
            <input
              v-model="profileForm.phoneNumber"
              type="text"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400"
            />
          </div>
          <p v-if="profileError" class="text-red-500 text-xs">{{ profileError }}</p>
          <div class="flex gap-2 pt-1">
            <button
              @click="saveProfile"
              :disabled="savingProfile"
              class="bg-red-600 text-white text-sm font-semibold px-4 py-2 rounded-lg hover:bg-red-700 disabled:opacity-50"
            >{{ savingProfile ? 'Đang lưu...' : 'Lưu' }}</button>
            <button
              @click="cancelEditProfile"
              class="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100"
            >Hủy</button>
          </div>
        </div>
      </div>

      <!-- Addresses Card -->
      <div class="bg-white rounded-2xl shadow-sm p-6">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold">Địa chỉ giao hàng</h2>
          <button
            @click="openAddAddress"
            class="text-sm text-red-600 font-semibold hover:underline"
          >+ Thêm địa chỉ</button>
        </div>

        <div v-if="addresses.length === 0" class="text-sm text-gray-400 text-center py-6">
          Bạn chưa có địa chỉ nào.
        </div>

        <ul class="space-y-3">
          <li
            v-for="addr in addresses"
            :key="addr.id"
            class="border rounded-xl p-4 text-sm"
            :class="addr.isDefault ? 'border-red-300 bg-red-50' : 'border-gray-100'"
          >
            <div class="flex items-start justify-between gap-2">
              <div>
                <p class="font-medium">{{ addr.street }}</p>
                <p class="text-gray-500">{{ addr.city }}</p>
                <span
                  v-if="addr.isDefault"
                  class="inline-block mt-1 text-xs text-red-600 font-semibold bg-red-100 px-2 py-0.5 rounded-full"
                >Mặc định</span>
              </div>
              <div class="flex flex-col gap-1 items-end shrink-0">
                <button @click="openEditAddress(addr)" class="text-xs text-blue-500 hover:underline">Sửa</button>
                <button @click="removeAddress(addr.id)" class="text-xs text-red-400 hover:underline">Xóa</button>
                <button
                  v-if="!addr.isDefault"
                  @click="makeDefault(addr.id)"
                  class="text-xs text-gray-500 hover:underline"
                >Đặt mặc định</button>
              </div>
            </div>
          </li>
        </ul>
      </div>

    </div>

    <!-- Address Modal -->
    <div
      v-if="showAddressModal"
      class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4"
      @click.self="closeAddressModal"
    >
      <div class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl">
        <h3 class="text-lg font-bold mb-4">{{ editingAddress ? 'Sửa địa chỉ' : 'Thêm địa chỉ' }}</h3>
        <div class="space-y-3">
          <div>
            <label class="text-sm text-gray-500 block mb-1">Địa chỉ (đường, số nhà)</label>
            <input
              v-model="addressForm.street"
              type="text"
              placeholder="Ví dụ: 123 Nguyễn Huệ"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400"
            />
          </div>
          <div>
            <label class="text-sm text-gray-500 block mb-1">Thành phố</label>
            <input
              v-model="addressForm.city"
              type="text"
              placeholder="Ví dụ: Hồ Chí Minh"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-red-400"
            />
          </div>
          <p v-if="addressError" class="text-red-500 text-xs">{{ addressError }}</p>
          <div class="flex gap-2 pt-1">
            <button
              @click="saveAddress"
              :disabled="savingAddress"
              class="bg-red-600 text-white text-sm font-semibold px-4 py-2 rounded-lg hover:bg-red-700 disabled:opacity-50"
            >{{ savingAddress ? 'Đang lưu...' : 'Lưu' }}</button>
            <button
              @click="closeAddressModal"
              class="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100"
            >Hủy</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { profileApi } from '../api/profile'

const profile = ref({ fullName: '', email: '', phoneNumber: '' })
const addresses = ref([])

const editingProfile = ref(false)
const savingProfile = ref(false)
const profileError = ref('')
const profileForm = ref({ fullName: '', phoneNumber: '' })

const showAddressModal = ref(false)
const editingAddress = ref(null)
const savingAddress = ref(false)
const addressError = ref('')
const addressForm = ref({ street: '', city: '' })

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

function startEditProfile() {
  profileForm.value = { fullName: profile.value.fullName || '', phoneNumber: profile.value.phoneNumber || '' }
  profileError.value = ''
  editingProfile.value = true
}

function cancelEditProfile() {
  editingProfile.value = false
}

async function saveProfile() {
  if (!profileForm.value.fullName.trim()) {
    profileError.value = 'Vui lòng nhập họ và tên'
    return
  }
  savingProfile.value = true
  profileError.value = ''
  try {
    profile.value = await profileApi.updateProfile(profileForm.value)
    editingProfile.value = false
  } catch {
    profileError.value = 'Cập nhật thất bại, thử lại sau'
  } finally {
    savingProfile.value = false
  }
}

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

function closeAddressModal() {
  showAddressModal.value = false
}

async function saveAddress() {
  if (!addressForm.value.street.trim()) {
    addressError.value = 'Vui lòng nhập địa chỉ'
    return
  }
  savingAddress.value = true
  addressError.value = ''
  try {
    if (editingAddress.value) {
      const updated = await profileApi.updateAddress(editingAddress.value.id, addressForm.value)
      const idx = addresses.value.findIndex(a => a.id === updated.id)
      if (idx !== -1) addresses.value[idx] = updated
    } else {
      const created = await profileApi.addAddress(addressForm.value)
      addresses.value.push(created)
    }
    showAddressModal.value = false
  } catch {
    addressError.value = 'Lưu địa chỉ thất bại, thử lại sau'
  } finally {
    savingAddress.value = false
  }
}

async function removeAddress(id) {
  await profileApi.deleteAddress(id)
  addresses.value = addresses.value.filter(a => a.id !== id)
}

async function makeDefault(id) {
  const updated = await profileApi.setDefaultAddress(id)
  addresses.value = addresses.value.map(a => ({ ...a, isDefault: a.id === updated.id }))
}
</script>
