<script setup>

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
    .format(value).replace('₫', 'đ')
}
</script>

<template>
  <div class="w-full bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow duration-300 flex flex-col overflow-hidden border border-gray-100 h-full">
    
    <div class="relative h-48 w-full overflow-hidden">
      <img 
        :src="product.image" 
        :alt="product.name" 
        class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
      >
      <div v-if="product.isNew" class="absolute top-0 left-0 bg-red-600 text-white text-[10px] font-bold px-3 py-1 rounded-br-lg uppercase">
        Mới
      </div>
    </div>

    <div class="p-4 flex flex-col flex-grow">
      <div class="flex justify-between items-start mb-2 gap-2">
        <h3 class="font-bold text-gray-900 leading-tight uppercase text-sm md:text-base flex-1">
          {{ product.name }}
        </h3>
        <div class="text-right shrink-0">
          <p class="font-bold text-gray-900 text-sm md:text-base">
            {{ formatPrice(product.price) }}
          </p>
          <p v-if="product.originalPrice" class="text-[11px] text-gray-400 line-through decoration-gray-400">
            {{ formatPrice(product.originalPrice) }}
          </p>
        </div>
      </div>

      <p class="text-[12px] text-gray-500 line-clamp-3 mb-6 leading-relaxed flex-grow">
        {{ product.description }}
      </p>

      <div class="mt-auto">
        <button class="w-full py-2.5 border-2 border-red-600 text-red-600 font-bold rounded-full text-sm hover:bg-red-600 hover:text-white transition-all duration-300 active:scale-95">
          Thêm
        </button>
      </div>
    </div>
  </div>
</template>