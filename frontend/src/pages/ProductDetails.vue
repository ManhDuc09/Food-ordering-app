<script setup>
import { ref } from 'vue'


const product = ref({
  id: 201,
  name: '1 Bánh Wrap Xốt Nhiệt Đới',
  price: 35000,
  description: 'Bánh Wrap Gà Tenders giòn cuộn tortilla nướng mềm mại, quyện xốt xoài chanh dây - Chua ngọt xao xuyến, nghỉ tay xóa miếng!',
  image: 'https://placehold.co/600x400?text=Product+Image',
  isNew: true
})

const quantity = ref(1)

const increment = () => quantity.value++
const decrement = () => {
  if (quantity.value > 1) quantity.value--
}

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
    .format(value).replace('₫', 'đ')
}
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-10">
    <nav class="text-xs text-gray-500 mb-6">
      <a href="#" class="hover:text-black">Món Mới</a>
      <span class="mx-2">></span>
      <span class="text-black font-medium">{{ product.name }}</span>
    </nav>

    <div class="flex flex-col md:flex-row gap-12 items-start">
      
      <div class="w-full md:w-1/2 relative">
        <div class="bg-gray-100 rounded-lg overflow-hidden">
          <img :src="product.image" :alt="product.name" class="w-full h-auto object-cover" />
        </div>
        <div v-if="product.isNew" class="absolute top-4 left-4 bg-red-600 text-white font-bold px-4 py-1 text-xl italic uppercase shadow-lg">
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
            {{ product.name }}
          </h1>

          <p class="text-gray-600 text-sm leading-relaxed mb-10">
            {{ product.description }}
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

            <button class="bg-red-600 text-white font-bold py-3 px-8 rounded-full shadow-lg shadow-red-200 hover:bg-red-700 transition-all active:scale-95 flex-grow ml-6">
              Thêm vào giỏ ({{ formatPrice(product.price * quantity) }})
            </button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>