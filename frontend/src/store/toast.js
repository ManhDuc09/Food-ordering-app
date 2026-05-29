import { reactive } from 'vue'

const toasts = reactive([])
let _id = 0

export function showToast(message, type = 'success', duration = 3000) {
  const id = ++_id
  toasts.push({ id, message, type })
  setTimeout(() => {
    const i = toasts.findIndex(t => t.id === id)
    if (i !== -1) toasts.splice(i, 1)
  }, duration)
}

export function dismissToast(id) {
  const i = toasts.findIndex(t => t.id === id)
  if (i !== -1) toasts.splice(i, 1)
}

export { toasts }
