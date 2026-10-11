import type { ApiError, Cart, Order, OrderPosition, Product } from './types'

const CUSTOMER_ID = '1'

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`/api${path}`, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...options?.headers },
  })
  if (!response.ok) {
    const body = (await response.json().catch(() => ({}))) as ApiError
    throw new Error(body.error || `Request failed (${response.status})`)
  }
  return response.json() as Promise<T>
}

export const api = {
  products: () => request<Product[]>('/products'),
  cart: () => request<Cart>(`/customers/${CUSTOMER_ID}/shoppingcart`),
  createOrder: () => request<Order>('/orders', {
    method: 'POST',
    body: JSON.stringify({ customerId: CUSTOMER_ID }),
  }),
  addPosition: (orderId: string, productId: string, quantity: number) =>
    request<OrderPosition>(`/orders/${orderId}/positions`, {
      method: 'POST',
      body: JSON.stringify({ productId, quantity }),
    }),
  checkout: (orderId: string) => request<Order>(`/orders/${orderId}/checkout`, { method: 'POST' }),
}
