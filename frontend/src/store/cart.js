import { reactive } from 'vue'

const CART_STORAGE_KEY = 'kfc_cart'

const state = reactive({
  items: JSON.parse(localStorage.getItem(CART_STORAGE_KEY) || '[]')
})

const persist = () => {
  localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(state.items))
}

const findItem = (productId) => state.items.find((item) => item.productId === productId)

export const cartState = state

export const getCartItems = () => state.items

export const getCartCount = () => state.items.reduce((total, item) => total + item.quantity, 0)

export const addToCart = (product, quantity = 1) => {
  const productId = product?.id || product?.productId
  if (!product || !productId || quantity <= 0) {
    return false
  }

  const existing = findItem(productId)
  if (existing) {
    existing.quantity += quantity
  } else {
    state.items.push({
      productId,
      name: product.name || product.title || 'Sản phẩm',
      price: Number(product.price) || 0,
      imageUrl: product.imageUrl || product.image || 'https://placehold.co/120x120?text=No+Image',
      quantity,
      available: product.isAvailable
    })
  }

  persist()
  return true
}

export const updateCartQuantity = (productId, quantity) => {
  const item = findItem(productId)
  if (!item) {
    return
  }

  if (quantity <= 0) {
    state.items = state.items.filter((entry) => entry.productId !== productId)
  } else {
    item.quantity = quantity
  }

  persist()
}

export const clearCart = () => {
  state.items.splice(0, state.items.length)
  persist()
}
