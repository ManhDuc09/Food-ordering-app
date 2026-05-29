<template>
  <Teleport to="body">
    <div class="fixed top-5 right-5 z-[9998] flex flex-col gap-3 pointer-events-none" style="max-width: 360px; width: calc(100vw - 2.5rem)">
      <TransitionGroup name="toast">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          class="flex items-start gap-3 rounded-2xl px-4 py-3 shadow-lg pointer-events-auto"
          :class="styleMap[toast.type]?.bg"
        >
          <!-- Icon -->
          <span class="mt-0.5 shrink-0 text-lg">{{ styleMap[toast.type]?.icon }}</span>

          <!-- Message -->
          <p class="text-sm font-semibold flex-1" :class="styleMap[toast.type]?.text">
            {{ toast.message }}
          </p>

          <!-- Close -->
          <button
            @click="dismissToast(toast.id)"
            class="shrink-0 opacity-50 hover:opacity-100 transition-opacity"
            :class="styleMap[toast.type]?.text"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup>
import { toasts, dismissToast } from '@/store/toast.js'

const styleMap = {
  success: {
    bg: 'bg-green-600',
    text: 'text-white',
    icon: '✓',
  },
  error: {
    bg: 'bg-[#E4002B]',
    text: 'text-white',
    icon: '✕',
  },
  info: {
    bg: 'bg-gray-800',
    text: 'text-white',
    icon: 'ℹ',
  },
}
</script>

<style scoped>
.toast-enter-active {
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.toast-leave-active {
  transition: all 0.2s ease-in;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(60px) scale(0.95);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(60px) scale(0.95);
}
.toast-move {
  transition: transform 0.25s ease;
}
</style>
