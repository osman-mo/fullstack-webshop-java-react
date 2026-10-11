export type Product = {
  id: string
  name: string
  description: string
  priceInCent: number
  tags: string[]
}

export type Order = {
  id: string
  customerId: string
  orderTime: string
  status: 'NEW' | 'CONFIRMED' | 'SENT' | 'DELIVERED' | 'CANCELLED'
}

export type OrderPosition = {
  id: string
  orderId: string
  productId: string
  quantity: number
}

export type Cart = {
  customerId: string
  orderPositions: OrderPosition[]
  totalCostInCent: number
  deliveryCostInCent: number
  deliveryOption: string
}

export type ApiError = { error?: string }
