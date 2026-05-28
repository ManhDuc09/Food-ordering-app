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
            <p class="text-red-200 text-xs">Cổng Quản Trị</p>
          </div>
        </div>
      </div>

      <nav class="flex-1 p-4 space-y-1">
        <button
          v-for="tab in tabs" :key="tab.value"
          @click="activeTab = tab.value"
          :class="['w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-bold transition-colors text-left',
            activeTab === tab.value ? 'bg-white text-[#E4002B]' : 'text-white hover:bg-red-700']"
        >
          <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-html="tab.icon"></svg>
          {{ tab.label }}
        </button>
      </nav>

      <div class="p-4 border-t border-red-700">
        <div class="mb-3 px-2">
          <p class="text-white font-bold text-sm truncate">{{ userEmail }}</p>
          <p class="text-red-200 text-xs">Quản trị viên</p>
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

    <!-- Main -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- Topbar -->
      <header class="bg-white shadow-sm px-8 py-4 flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-black text-gray-900 uppercase">{{ tabs.find(t => t.value === activeTab)?.label }}</h1>
          <p class="text-sm text-gray-500">Bảng điều khiển Admin</p>
        </div>
        <div class="flex items-center gap-3">
          <span class="text-sm text-gray-500">{{ today }}</span>
          <button
            @click="loadAll"
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

        <!-- Overview -->
        <div v-if="activeTab === 'overview'" class="space-y-6">
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-[#E4002B]">
              <p class="text-sm text-gray-500 font-bold uppercase">Tổng người dùng</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ users.length }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-yellow-400">
              <p class="text-sm text-gray-500 font-bold uppercase">Sản phẩm</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ products.length }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-blue-500">
              <p class="text-sm text-gray-500 font-bold uppercase">Danh mục</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ categories.length }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm border-l-4 border-green-500">
              <p class="text-sm text-gray-500 font-bold uppercase">Chi nhánh</p>
              <p class="text-3xl font-black text-gray-900 mt-1">{{ branches.length }}</p>
            </div>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <div class="bg-white rounded-xl p-6 shadow-sm">
              <p class="text-xs font-black text-gray-400 uppercase mb-1">Quản trị viên</p>
              <p class="text-2xl font-black text-gray-900">{{ countByRole('admin') }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm">
              <p class="text-xs font-black text-gray-400 uppercase mb-1">Quản lý</p>
              <p class="text-2xl font-black text-gray-900">{{ countByRole('branch_manager') }}</p>
            </div>
            <div class="bg-white rounded-xl p-6 shadow-sm">
              <p class="text-xs font-black text-gray-400 uppercase mb-1">Khách hàng</p>
              <p class="text-2xl font-black text-gray-900">{{ countByRole('customer') }}</p>
            </div>
          </div>
        </div>

        <!-- Users -->
        <div v-if="activeTab === 'users'">
          <div v-if="loading" class="flex justify-center py-16">
            <svg class="animate-spin w-10 h-10 text-[#E4002B]" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
            </svg>
          </div>
          <div v-else class="bg-white rounded-xl shadow-sm overflow-hidden">
            <table class="w-full text-sm">
              <thead class="bg-gray-50 border-b">
                <tr>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Họ tên</th>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Email</th>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Vai trò</th>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Chi nhánh</th>
                  <th class="px-6 py-3"></th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="u in users" :key="u.userId" class="hover:bg-gray-50">
                  <td class="px-6 py-4 font-bold text-gray-900">{{ u.fullName || '—' }}</td>
                  <td class="px-6 py-4 text-gray-600">{{ u.email }}</td>
                  <td class="px-6 py-4">
                    <span :class="roleClass(u.role)" class="px-2 py-1 rounded-full text-xs font-black uppercase">{{ roleLabel(u.role) }}</span>
                  </td>
                  <td class="px-6 py-4 text-gray-500">{{ u.branchName || '—' }}</td>
                  <td class="px-6 py-4 text-right">
                    <button @click="openUserModal(u)" class="text-[#E4002B] font-bold text-xs hover:underline">Sửa vai trò</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Products -->
        <div v-if="activeTab === 'products'">
          <div class="flex justify-end mb-4">
            <button @click="openProductModal(null)" class="bg-[#E4002B] text-white px-4 py-2 rounded-lg text-sm font-bold hover:bg-red-700 transition-colors">
              + Thêm sản phẩm
            </button>
          </div>
          <div class="bg-white rounded-xl shadow-sm overflow-hidden">
            <table class="w-full text-sm">
              <thead class="bg-gray-50 border-b">
                <tr>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Tên</th>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Giá</th>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Đang bán</th>
                  <th class="text-left px-6 py-3 font-black text-gray-500 uppercase text-xs">Danh mục</th>
                  <th class="px-6 py-3"></th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="p in products" :key="p.productId" class="hover:bg-gray-50">
                  <td class="px-6 py-4 font-bold text-gray-900">{{ p.name }}</td>
                  <td class="px-6 py-4 font-black text-[#E4002B]">{{ formatCurrency(p.price) }}</td>
                  <td class="px-6 py-4">
                    <span :class="p.isAvailable ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'" class="px-2 py-1 rounded-full text-xs font-bold">
                      {{ p.isAvailable ? 'Có' : 'Không' }}
                    </span>
                  </td>
                  <td class="px-6 py-4 text-gray-500 text-xs">{{ p.categoryNames?.join(', ') || '—' }}</td>
                  <td class="px-6 py-4 text-right space-x-3">
                    <button @click="openProductModal(p)" class="text-blue-600 font-bold text-xs hover:underline">Sửa</button>
                    <button @click="confirmDelete('product', p.productId)" class="text-red-500 font-bold text-xs hover:underline">Xóa</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Categories -->
        <div v-if="activeTab === 'categories'">
          <div class="flex gap-3 mb-6">
            <input
              v-model="newCategoryName"
              @keyup.enter="createCategory"
              placeholder="Tên danh mục mới..."
              class="flex-1 border border-gray-300 rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]"
            />
            <button @click="createCategory" class="bg-[#E4002B] text-white px-5 py-2 rounded-lg text-sm font-bold hover:bg-red-700 transition-colors">
              Thêm
            </button>
          </div>
          <div class="bg-white rounded-xl shadow-sm overflow-hidden">
            <div
              v-for="cat in categories" :key="cat.categoryId"
              class="flex items-center justify-between px-6 py-4 border-b last:border-0 hover:bg-gray-50"
            >
              <span class="font-bold text-gray-900">{{ cat.name }}</span>
              <button @click="confirmDelete('category', cat.categoryId)" class="text-red-500 font-bold text-xs hover:underline">Xóa</button>
            </div>
            <div v-if="categories.length === 0" class="px-6 py-8 text-center text-gray-400 font-bold">Chưa có danh mục</div>
          </div>
        </div>

        <!-- Branches -->
        <div v-if="activeTab === 'branches'">
          <div class="flex justify-end mb-4">
            <button @click="openBranchModal(null)" class="bg-[#E4002B] text-white px-4 py-2 rounded-lg text-sm font-bold hover:bg-red-700 transition-colors">
              + Thêm chi nhánh
            </button>
          </div>
          <div class="space-y-4">
            <div
              v-for="b in branches" :key="b.branchId"
              class="bg-white rounded-xl shadow-sm p-6 flex items-center justify-between"
            >
              <div>
                <p class="font-black text-gray-900">{{ b.name }}</p>
                <p class="text-gray-500 text-sm mt-0.5">{{ b.address }}</p>
                <span :class="b.isOpen ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'" class="px-2 py-0.5 rounded-full text-xs font-bold mt-2 inline-block">
                  {{ b.isOpen ? 'Đang mở' : 'Đóng cửa' }}
                </span>
              </div>
              <div class="flex gap-4">
                <button @click="openBranchModal(b)" class="text-blue-600 font-bold text-sm hover:underline">Sửa</button>
                <button @click="confirmDelete('branch', b.branchId)" class="text-red-500 font-bold text-sm hover:underline">Xóa</button>
              </div>
            </div>
            <div v-if="branches.length === 0" class="bg-white rounded-xl p-16 text-center shadow-sm">
              <p class="font-bold text-gray-400">Chưa có chi nhánh</p>
            </div>
          </div>
        </div>

      </main>
    </div>

    <!-- User Role Modal -->
    <div v-if="userModal.open" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-xl p-6 w-full max-w-sm shadow-2xl">
        <h3 class="font-black text-lg mb-1">Sửa vai trò người dùng</h3>
        <p class="text-sm text-gray-500 mb-5">{{ userModal.user?.email }}</p>
        <div class="space-y-4">
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Vai trò</label>
            <select v-model="userModal.role" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]">
              <option value="customer">Khách hàng</option>
              <option value="branch_manager">Quản lý chi nhánh</option>
              <option value="admin">Admin</option>
            </select>
          </div>
          <div v-if="userModal.role === 'branch_manager'">
            <label class="text-xs font-black text-gray-500 uppercase">Gán chi nhánh</label>
            <select v-model="userModal.branchId" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]">
              <option value="">Chưa gán chi nhánh</option>
              <option v-for="b in branches" :key="b.branchId" :value="b.branchId">{{ b.name }}</option>
            </select>
          </div>
        </div>
        <div class="flex gap-3 mt-6">
          <button @click="userModal.open = false" class="flex-1 border border-gray-300 rounded-lg py-2 text-sm font-bold text-gray-600 hover:bg-gray-50">Hủy</button>
          <button @click="submitUserRole" class="flex-1 bg-[#E4002B] text-white rounded-lg py-2 text-sm font-bold hover:bg-red-700">Lưu</button>
        </div>
      </div>
    </div>

    <!-- Product Modal -->
    <div v-if="productModal.open" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-xl p-6 w-full max-w-lg shadow-2xl max-h-[90vh] overflow-y-auto">
        <h3 class="font-black text-lg mb-5">{{ productModal.mode === 'create' ? 'Thêm sản phẩm' : 'Sửa sản phẩm' }}</h3>
        <div class="space-y-4">
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Tên sản phẩm</label>
            <input v-model="productModal.form.name" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]" />
          </div>
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Mô tả</label>
            <textarea v-model="productModal.form.description" rows="2" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]"></textarea>
          </div>
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Ảnh</label>
            <div class="mt-1 flex gap-2">
              <input
                v-model="productModal.form.imageUrl"
                placeholder="Dán URL hoặc tải ảnh lên"
                class="flex-1 border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]"
              />
              <label class="cursor-pointer flex items-center gap-1.5 bg-gray-100 hover:bg-gray-200 border border-gray-300 text-gray-700 font-bold text-xs px-3 py-2 rounded-lg transition-colors">
                <svg v-if="!imageUploading" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" />
                </svg>
                <svg v-else class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
                </svg>
                {{ imageUploading ? 'Đang tải...' : 'Tải lên' }}
                <input type="file" accept="image/*" class="hidden" @change="uploadImage" :disabled="imageUploading" />
              </label>
            </div>
            <div v-if="productModal.form.imageUrl" class="mt-2">
              <img :src="productModal.form.imageUrl" class="h-24 w-24 object-cover rounded-lg border border-gray-200" @error="e => e.target.style.display='none'" />
            </div>
          </div>
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Giá (VND)</label>
            <input v-model.number="productModal.form.price" type="number" min="0" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]" />
          </div>
          <div class="flex items-center gap-2">
            <input type="checkbox" v-model="productModal.form.isAvailable" id="prodAvailable" class="w-4 h-4 accent-[#E4002B]" />
            <label for="prodAvailable" class="text-sm font-bold text-gray-700">Đang bán</label>
          </div>
          <div>
            <label class="text-xs font-black text-gray-500 uppercase mb-2 block">Categories</label>
            <select
              @change="e => { const id = e.target.value; if (id && !productModal.form.categoryIds.includes(id)) productModal.form.categoryIds.push(id); e.target.value = '' }"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]"
            >
              <option value="">Chọn danh mục...</option>
              <option
                v-for="cat in categories"
                :key="cat.categoryId"
                :value="cat.categoryId"
                :disabled="productModal.form.categoryIds.includes(cat.categoryId)"
              >{{ cat.name }}</option>
            </select>
            <div class="flex flex-wrap gap-2 mt-2">
              <span
                v-for="id in productModal.form.categoryIds"
                :key="id"
                class="flex items-center gap-1 bg-red-100 text-[#E4002B] text-xs font-bold px-3 py-1 rounded-full"
              >
                {{ categories.find(c => c.categoryId === id)?.name }}
                <button type="button" @click="productModal.form.categoryIds = productModal.form.categoryIds.filter(c => c !== id)" class="ml-1 hover:text-red-800">&times;</button>
              </span>
            </div>
          </div>
        </div>
        <div class="flex gap-3 mt-6">
          <button @click="productModal.open = false" class="flex-1 border border-gray-300 rounded-lg py-2 text-sm font-bold text-gray-600 hover:bg-gray-50">Hủy</button>
          <button @click="submitProduct" class="flex-1 bg-[#E4002B] text-white rounded-lg py-2 text-sm font-bold hover:bg-red-700">Lưu</button>
        </div>
      </div>
    </div>

    <!-- Branch Modal -->
    <div v-if="branchModal.open" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-xl p-6 w-full max-w-md shadow-2xl">
        <h3 class="font-black text-lg mb-5">{{ branchModal.mode === 'create' ? 'Thêm chi nhánh' : 'Sửa chi nhánh' }}</h3>
        <div class="space-y-4">
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Tên chi nhánh</label>
            <input v-model="branchModal.form.name" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]" />
          </div>
          <div>
            <label class="text-xs font-black text-gray-500 uppercase">Địa chỉ</label>
            <textarea v-model="branchModal.form.address" rows="2" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]"></textarea>
          </div>
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="text-xs font-black text-gray-500 uppercase">Vĩ độ</label>
              <input v-model.number="branchModal.form.latitude" type="number" step="any" placeholder="e.g. 10.762622" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]" />
            </div>
            <div>
              <label class="text-xs font-black text-gray-500 uppercase">Kinh độ</label>
              <input v-model.number="branchModal.form.longitude" type="number" step="any" placeholder="e.g. 106.660172" class="w-full border border-gray-300 rounded-lg px-3 py-2 mt-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#E4002B]" />
            </div>
          </div>
          <div class="flex items-center gap-2">
            <input type="checkbox" v-model="branchModal.form.isOpen" id="branchOpen" class="w-4 h-4 accent-[#E4002B]" />
            <label for="branchOpen" class="text-sm font-bold text-gray-700">Đang mở cửa</label>
          </div>
        </div>
        <div class="flex gap-3 mt-6">
          <button @click="branchModal.open = false" class="flex-1 border border-gray-300 rounded-lg py-2 text-sm font-bold text-gray-600 hover:bg-gray-50">Hủy</button>
          <button @click="submitBranch" class="flex-1 bg-[#E4002B] text-white rounded-lg py-2 text-sm font-bold hover:bg-red-700">Lưu</button>
        </div>
      </div>
    </div>

    <!-- Confirm Delete Modal -->
    <div v-if="deleteModal.open" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-xl p-6 w-full max-w-xs shadow-2xl text-center">
        <p class="font-black text-lg mb-2">Xóa mục này?</p>
        <p class="text-gray-500 text-sm mb-6">Hành động này không thể hoàn tác.</p>
        <div class="flex gap-3">
          <button @click="deleteModal.open = false" class="flex-1 border border-gray-300 rounded-lg py-2 text-sm font-bold text-gray-600 hover:bg-gray-50">Hủy</button>
          <button @click="executeDelete" class="flex-1 bg-[#E4002B] text-white rounded-lg py-2 text-sm font-bold hover:bg-red-700">Xóa</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { adminApi } from '../api/admin'
import { fetchProducts } from '../api/product'
import { fetchCategories } from '../api/categories'
import { branchApi } from '../api/branch'
import { logout } from '../store/auth'

const router = useRouter()

const user = JSON.parse(sessionStorage.getItem('user') || 'null')
const userEmail = user?.email || ''

const today = new Date().toLocaleDateString('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })

const activeTab = ref('overview')
const loading = ref(false)
const imageUploading = ref(false)

const CLOUDINARY_CLOUD = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME
const CLOUDINARY_PRESET = import.meta.env.VITE_CLOUDINARY_UPLOAD_PRESET

const users = ref([])
const products = ref([])
const categories = ref([])
const branches = ref([])
const newCategoryName = ref('')

const tabs = [
  {
    value: 'overview', label: 'Tổng quan',
    icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />'
  },
  {
    value: 'users', label: 'Người dùng',
    icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />'
  },
  {
    value: 'products', label: 'Sản phẩm',
    icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />'
  },
  {
    value: 'categories', label: 'Danh mục',
    icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />'
  },
  {
    value: 'branches', label: 'Chi nhánh',
    icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" /><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />'
  },
]

// ── Modals ──────────────────────────────────────────────

const userModal = reactive({ open: false, user: null, role: '', branchId: '' })
const productModal = reactive({
  open: false, mode: 'create', productId: null,
  form: { name: '', description: '', imageUrl: '', price: 0, isAvailable: true, categoryIds: [] }
})
const branchModal = reactive({
  open: false, mode: 'create', branchId: null,
  form: { name: '', address: '', latitude: null, longitude: null, isOpen: true }
})
const deleteModal = reactive({ open: false, type: '', id: null })

// ── Helpers ──────────────────────────────────────────────

const formatCurrency = (amount) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount)

const countByRole = (role) => users.value.filter(u => u.role === role).length

const roleLabel = (role) => ({
  admin: 'Admin',
  branch_manager: 'Quản lý',
  customer: 'Khách hàng',
}[role] || role)

const roleClass = (role) => ({
  admin: 'bg-purple-100 text-purple-700',
  branch_manager: 'bg-blue-100 text-blue-700',
  customer: 'bg-gray-100 text-gray-600',
}[role] || 'bg-gray-100 text-gray-600')

// ── Data loading ─────────────────────────────────────────

const loadAll = async () => {
  loading.value = true
  const [u, p, c, b] = await Promise.allSettled([
    adminApi.getUsers(),
    fetchProducts(),
    fetchCategories(),
    branchApi.getAllBranches(),
  ])
  if (u.status === 'fulfilled') users.value = u.value || []
  else console.error('Failed to load users:', u.reason)
  if (p.status === 'fulfilled') products.value = p.value || []
  if (c.status === 'fulfilled') categories.value = c.value || []
  if (b.status === 'fulfilled') branches.value = b.value || []
  loading.value = false
}

// ── Users ─────────────────────────────────────────────────

const openUserModal = (u) => {
  userModal.user = u
  userModal.role = u.role
  userModal.branchId = u.branchId || ''
  userModal.open = true
}

const submitUserRole = async () => {
  try {
    const updated = await adminApi.updateUserRole(userModal.user.userId, userModal.role, userModal.branchId || null)
    const idx = users.value.findIndex(u => u.userId === userModal.user.userId)
    if (idx !== -1) users.value[idx] = updated
    userModal.open = false
  } catch (e) {
    alert(e.message)
  }
}

// ── Products ──────────────────────────────────────────────

const openProductModal = (p) => {
  if (p) {
    productModal.mode = 'edit'
    productModal.productId = p.productId
    productModal.form = {
      name: p.name,
      description: p.description || '',
      imageUrl: p.imageUrl || '',
      price: p.price,
      isAvailable: p.isAvailable,
      categoryIds: categories.value
        .filter(c => p.categoryNames?.includes(c.name))
        .map(c => c.categoryId),
    }
  } else {
    productModal.mode = 'create'
    productModal.productId = null
    productModal.form = { name: '', description: '', imageUrl: '', price: 0, isAvailable: true, categoryIds: [] }
  }
  productModal.open = true
}

const submitProduct = async () => {
  try {
    const payload = { ...productModal.form }
    if (productModal.mode === 'create') {
      const created = await adminApi.createProduct(payload)
      products.value.unshift(created)
    } else {
      const updated = await adminApi.updateProduct(productModal.productId, payload)
      const idx = products.value.findIndex(p => p.productId === productModal.productId)
      if (idx !== -1) products.value[idx] = updated
    }
    productModal.open = false
  } catch (e) {
    alert(e.message)
  }
}

// ── Categories ────────────────────────────────────────────

const createCategory = async () => {
  const name = newCategoryName.value.trim()
  if (!name) return
  try {
    const created = await adminApi.createCategory(name)
    categories.value.push(created)
    newCategoryName.value = ''
  } catch (e) {
    alert(e.message)
  }
}

// ── Branches ──────────────────────────────────────────────

const openBranchModal = (b) => {
  if (b) {
    branchModal.mode = 'edit'
    branchModal.branchId = b.branchId
    branchModal.form = {
      name: b.name,
      address: b.address || '',
      latitude: b.latitude,
      longitude: b.longitude,
      isOpen: b.isOpen,
    }
  } else {
    branchModal.mode = 'create'
    branchModal.branchId = null
    branchModal.form = { name: '', address: '', latitude: null, longitude: null, isOpen: true }
  }
  branchModal.open = true
}

const submitBranch = async () => {
  try {
    if (branchModal.mode === 'create') {
      const created = await adminApi.createBranch(branchModal.form)
      branches.value.push(created)
    } else {
      const updated = await adminApi.updateBranch(branchModal.branchId, branchModal.form)
      const idx = branches.value.findIndex(b => b.branchId === branchModal.branchId)
      if (idx !== -1) branches.value[idx] = updated
    }
    branchModal.open = false
  } catch (e) {
    alert(e.message)
  }
}

// ── Delete ────────────────────────────────────────────────

const confirmDelete = (type, id) => {
  deleteModal.type = type
  deleteModal.id = id
  deleteModal.open = true
}

const executeDelete = async () => {
  try {
    if (deleteModal.type === 'product') {
      await adminApi.deleteProduct(deleteModal.id)
      products.value = products.value.filter(p => p.productId !== deleteModal.id)
    } else if (deleteModal.type === 'category') {
      await adminApi.deleteCategory(deleteModal.id)
      categories.value = categories.value.filter(c => c.categoryId !== deleteModal.id)
    } else if (deleteModal.type === 'branch') {
      await adminApi.deleteBranch(deleteModal.id)
      branches.value = branches.value.filter(b => b.branchId !== deleteModal.id)
    }
    deleteModal.open = false
  } catch (e) {
    alert(e.message)
  }
}

// ── Image Upload ──────────────────────────────────────────

const uploadImage = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  imageUploading.value = true
  try {
    const form = new FormData()
    form.append('file', file)
    form.append('upload_preset', CLOUDINARY_PRESET)
    const res = await fetch(`https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD}/image/upload`, {
      method: 'POST',
      body: form
    })
    if (!res.ok) throw new Error('Upload failed')
    const data = await res.json()
    productModal.form.imageUrl = data.secure_url
  } catch (e) {
    alert('Tải ảnh thất bại: ' + e.message)
  } finally {
    imageUploading.value = false
    e.target.value = ''
  }
}

// ── Auth ──────────────────────────────────────────────────

const handleLogout = () => {
  logout()
  router.push('/login')
}

onMounted(loadAll)
</script>
