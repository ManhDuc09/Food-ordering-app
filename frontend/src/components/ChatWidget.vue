<template>
  <!-- Chat bubble button -->
  <button
    @click="open = !open"
    class="fixed bottom-6 right-6 z-50 w-14 h-14 bg-[#E4002B] text-white rounded-full shadow-lg flex items-center justify-center hover:bg-red-700 transition-all"
    :class="open ? 'scale-90' : 'hover:scale-105'"
  >
    <svg v-if="!open" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
        d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
    </svg>
    <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12" />
    </svg>
  </button>

  <!-- Chat window -->
  <Transition name="chat">
    <div
      v-if="open"
      class="fixed bottom-24 right-6 z-50 w-80 bg-white rounded-2xl shadow-2xl flex flex-col overflow-hidden border border-gray-100"
      style="height: 440px"
    >
      <!-- Header -->
      <div class="bg-[#E4002B] px-4 py-3 flex items-center gap-3">
        <div class="w-8 h-8 bg-white rounded-full flex items-center justify-center shrink-0">
          <img src="https://upload.wikimedia.org/wikipedia/sco/b/bf/KFC_logo.svg" class="w-5 h-5 object-contain" />
        </div>
        <div>
          <p class="text-white font-bold text-sm leading-none">KFC Trợ lý</p>
          <p class="text-red-200 text-xs">Luôn sẵn sàng hỗ trợ bạn</p>
        </div>
      </div>

      <!-- Messages -->
      <div ref="messagesEl" class="flex-1 overflow-y-auto p-4 space-y-3 bg-gray-50">
        <div v-for="(msg, i) in messages" :key="i" class="flex" :class="msg.role === 'user' ? 'justify-end' : 'justify-start'">
          <div
            class="max-w-[80%] px-3 py-2 rounded-2xl text-sm leading-relaxed"
            :class="msg.role === 'user'
              ? 'bg-[#E4002B] text-white rounded-br-sm'
              : 'bg-white text-gray-800 shadow-sm border border-gray-100 rounded-bl-sm'"
          >
            {{ msg.content }}
          </div>
        </div>

        <!-- Typing indicator -->
        <div v-if="loading" class="flex justify-start">
          <div class="bg-white border border-gray-100 shadow-sm px-4 py-3 rounded-2xl rounded-bl-sm flex gap-1 items-center">
            <span class="w-1.5 h-1.5 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 0ms"></span>
            <span class="w-1.5 h-1.5 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 150ms"></span>
            <span class="w-1.5 h-1.5 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 300ms"></span>
          </div>
        </div>
      </div>

      <!-- Input -->
      <div class="p-3 bg-white border-t border-gray-100 flex gap-2">
        <input
          v-model="input"
          @keydown.enter.prevent="send"
          type="text"
          placeholder="Nhắn tin..."
          :disabled="loading"
          class="flex-1 rounded-full border border-gray-200 px-4 py-2 text-sm focus:outline-none focus:border-red-400 disabled:opacity-50"
        />
        <button
          @click="send"
          :disabled="loading || !input.trim()"
          class="w-9 h-9 bg-[#E4002B] text-white rounded-full flex items-center justify-center hover:bg-red-700 disabled:opacity-40 transition-colors shrink-0"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
          </svg>
        </button>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { ref, nextTick } from 'vue'

const open = ref(false)
const input = ref('')
const loading = ref(false)
const messagesEl = ref(null)

const messages = ref([
  { role: 'assistant', content: 'Xin chào! Tôi là trợ lý KFC. Tôi có thể giúp gì cho bạn hôm nay? 🍗' }
])

const scrollToBottom = () => nextTick(() => {
  if (messagesEl.value) messagesEl.value.scrollTop = messagesEl.value.scrollHeight
})

const send = async () => {
  const text = input.value.trim()
  if (!text || loading.value) return

  messages.value.push({ role: 'user', content: text })
  input.value = ''
  loading.value = true
  scrollToBottom()

  try {
    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ messages: messages.value })
    })
    const data = await res.json()
    messages.value.push({ role: 'assistant', content: data.reply })
  } catch {
    messages.value.push({ role: 'assistant', content: 'Xin lỗi, có lỗi xảy ra. Vui lòng thử lại.' })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}
</script>

<style scoped>
.chat-enter-active { transition: all 0.25s cubic-bezier(0.34, 1.56, 0.64, 1); }
.chat-leave-active { transition: all 0.2s ease-in; }
.chat-enter-from { opacity: 0; transform: scale(0.9) translateY(16px); transform-origin: bottom right; }
.chat-leave-to { opacity: 0; transform: scale(0.9) translateY(16px); transform-origin: bottom right; }
</style>
