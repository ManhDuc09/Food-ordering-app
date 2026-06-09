export const OrderStatus = Object.freeze({
  PENDING: 'pending',
  CONFIRMED: 'confirmed',
  DELIVERED: 'delivered',
  CANCELLED: 'cancelled',
})

export const PaymentStatus = Object.freeze({
  PENDING: 'pending',
  PAID: 'paid',
})

export const PaymentMethod = Object.freeze({
  COD: 'COD',
  MOMO: 'MOMO',
  VNPAY: 'VNPAY',
})
