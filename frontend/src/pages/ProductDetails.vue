<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { fetchProduct } from '@/api/product.js'
import { addToCart } from '@/store/cart.js'

const route = useRoute()
const product = ref(null)
const quantity = ref(1)
const isLoading = ref(true)
const error = ref('')
const successMessage = ref('')

const increment = () => quantity.value++
const decrement = () => {
  if (quantity.value > 1) quantity.value--
}

const addProductToCart = () => {
  if (!product.value) {
    return
  }

  const added = addToCart(product.value, quantity.value)
  if (added) {
    successMessage.value = 'Đã thêm sản phẩm vào giỏ hàng.'
  } else {
    successMessage.value = 'Không thể thêm sản phẩm vào giỏ hàng.'
  }

  setTimeout(() => {
    successMessage.value = ''
  }, 2500)
}

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
    .format(value).replace('₫', 'đ')
}

const loadProduct = async () => {
  const id = route.params.id
  if (!id) {
    error.value = 'Sản phẩm không hợp lệ.'
    isLoading.value = false
    return
  }

  const result = await fetchProduct(id)
  if (!result) {
    error.value = 'Không tìm thấy sản phẩm.'
  } else {
    product.value = result
  }

  isLoading.value = false
}

const imageUrl = computed(() => product.value?.imageUrl || 'https://placehold.co/600x400?text=No+Image')
const title = computed(() => product.value?.name || 'Đang tải sản phẩm...')
const description = computed(() => product.value?.description || 'Mô tả sản phẩm sẽ có trong vài giây.')
const price = computed(() => Number(product.value?.price) || 0)
const isNew = computed(() => product.value?.isAvailable === true)

onMounted(loadProduct)
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-10">
    <div v-if="isLoading" class="text-center py-20 text-gray-500">
      Đang tải thông tin sản phẩm...
    </div>

    <div v-else-if="error" class="text-center py-20 text-red-500">
      {{ error }}
    </div>

    <div v-else>
      <nav class="text-xs text-gray-500 mb-6">
        <router-link to="/menu" class="hover:text-black">Menu</router-link>
        <span class="mx-2">></span>
        <span class="text-black font-medium">{{ title }}</span>
      </nav>

      <div class="flex flex-col md:flex-row gap-12 items-start">
        <div class="w-full md:w-1/2 relative">
          <div class="bg-gray-100 rounded-lg overflow-hidden">
            <img :src="imageUrl" :alt="title" class="w-full h-auto object-cover" />
          </div>
          <div v-if="isNew" class="absolute top-4 left-4 bg-red-600 text-white font-bold px-4 py-1 text-xl italic uppercase shadow-lg">
            Mới!
          </div>
        </div>

        <div class="w-full md:w-1/2">
          <div class="bg-white p-8 rounded-xl shadow-sm border border-gray-100 relative">
            <div class="flex gap-1 mb-6">
              <div class="w-2 h-8 bg-red-600"></div>
              <div class="w-2 h-8 bg-red-600"></div>
              <div class="w-2 h-8 bg-red-600"></div>
            </div>

            <h1 class="text-3xl font-black text-gray-900 uppercase mb-4 leading-tight">
              {{ title }}
            </h1>

            <p class="text-gray-600 text-sm leading-relaxed mb-10">
              {{ description }}
            </p>

            <div class="flex items-center justify-between mt-auto pt-6 border-t border-gray-100">
              <div class="flex items-center border-2 border-gray-200 rounded-full px-2 py-1">
                <button @click="decrement" class="w-8 h-8 flex items-center justify-center text-2xl font-light hover:text-red-600 transition">
                  −
                </button>
                <span class="w-10 text-center font-bold text-lg">{{ quantity }}</span>
                <button @click="increment" class="w-8 h-8 flex items-center justify-center text-2xl font-light hover:text-red-600 transition">
                  +
                </button>
              </div>

              <button @click="addProductToCart" class="bg-red-600 text-white font-bold py-3 px-8 rounded-full shadow-lg shadow-red-200 hover:bg-red-700 transition-all active:scale-95 flex-grow ml-6">
                Thêm vào giỏ ({{ formatPrice(price * quantity) }})
              </button>
            </div>
            <div v-if="successMessage" class="mt-4 text-sm text-green-600 font-medium">
              {{ successMessage }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>