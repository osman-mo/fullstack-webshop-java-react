import { useEffect, useMemo, useState } from 'react'
import { api } from './api'
import type { Cart, Product } from './types'

const ORDER_KEY = 'component-shop-order-id'
const euro = new Intl.NumberFormat('de-DE', { style: 'currency', currency: 'EUR' })
const money = (cent: number) => euro.format(cent / 100)

function App() {
  const [products, setProducts] = useState<Product[]>([])
  const [cart, setCart] = useState<Cart | null>(null)
  const [query, setQuery] = useState('')
  const [tag, setTag] = useState('All')
  const [busyId, setBusyId] = useState<string | null>(null)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    void Promise.all([api.products(), api.cart()])
      .then(([nextProducts, nextCart]) => {
        setProducts(nextProducts)
        setCart(nextCart)
      })
      .catch((reason: Error) => setError(reason.message))
  }, [])

  const tags = useMemo(
    () => ['All', ...Array.from(new Set(products.flatMap((product) => product.tags))).sort()],
    [products],
  )

  const visibleProducts = useMemo(() => {
    const needle = query.trim().toLowerCase()
    return products.filter((product) => {
      const matchesTag = tag === 'All' || product.tags.includes(tag)
      const searchable = `${product.name} ${product.description} ${product.tags.join(' ')}`.toLowerCase()
      return matchesTag && (!needle || searchable.includes(needle))
    })
  }, [products, query, tag])

  const cartLines = useMemo(() => {
    if (!cart) return []
    const grouped = new Map<string, number>()
    for (const position of cart.orderPositions) {
      grouped.set(position.productId, (grouped.get(position.productId) ?? 0) + position.quantity)
    }
    return Array.from(grouped.entries()).flatMap(([productId, quantity]) => {
      const product = products.find((item) => item.id === productId)
      return product ? [{ product, quantity }] : []
    })
  }, [cart, products])

  const addToCart = async (productId: string) => {
    setBusyId(productId)
    setError('')
    setMessage('')
    try {
      let orderId = localStorage.getItem(ORDER_KEY)
      if (!orderId) {
        const order = await api.createOrder()
        orderId = order.id
        localStorage.setItem(ORDER_KEY, orderId)
      }
      try {
        await api.addPosition(orderId, productId, 1)
      } catch {
        const replacement = await api.createOrder()
        orderId = replacement.id
        localStorage.setItem(ORDER_KEY, orderId)
        await api.addPosition(orderId, productId, 1)
      }
      setCart(await api.cart())
      setMessage('Added to cart')
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Could not add the product')
    } finally {
      setBusyId(null)
    }
  }

  const checkout = async () => {
    const orderId = localStorage.getItem(ORDER_KEY)
    if (!orderId || !cart?.orderPositions.length) return
    setBusyId('checkout')
    setError('')
    try {
      await api.checkout(orderId)
      localStorage.removeItem(ORDER_KEY)
      setCart(await api.cart())
      setMessage('Order confirmed — thank you!')
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Checkout failed')
    } finally {
      setBusyId(null)
    }
  }

  return (
    <div className="page-shell">
      <header className="topbar">
        <a className="brand" href="#top" aria-label="Component Shop home">
          <span className="brand-mark">CS</span>
          <span>Component Shop</span>
        </a>
        <div className="cart-pill">{cartLines.reduce((sum, line) => sum + line.quantity, 0)} items</div>
      </header>

      <main id="top">
        <section className="hero">
          <div>
            <p className="eyebrow">BUILD YOUR NEXT MACHINE</p>
            <h1>Reliable components.<br /><span>Clean performance.</span></h1>
            <p className="hero-copy">A compact full-stack shop demo with a real Java API and MySQL persistence.</p>
          </div>
          <div className="hero-stat"><strong>{products.length}</strong><span>components available</span></div>
        </section>

        {(error || message) && (
          <div className={error ? 'notice error' : 'notice success'} role="status">
            {error || message}
          </div>
        )}

        <div className="shop-layout">
          <section className="catalog" aria-label="Product catalog">
            <div className="catalog-tools">
              <div>
                <p className="section-kicker">CATALOG</p>
                <h2>Choose your components</h2>
              </div>
              <label className="search">
                <span>Search</span>
                <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="CPU, GPU, storage…" />
              </label>
            </div>

            <div className="filters" aria-label="Product filters">
              {tags.map((value) => (
                <button key={value} className={tag === value ? 'active' : ''} onClick={() => setTag(value)}>{value}</button>
              ))}
            </div>

            <div className="product-grid">
              {visibleProducts.map((product) => (
                <article className="product-card" key={product.id}>
                  <div className="product-art" aria-hidden="true"><span>{product.tags[0] ?? 'TECH'}</span></div>
                  <div className="product-info">
                    <div className="tag-row">{product.tags.slice(0, 3).map((item) => <span key={item}>{item}</span>)}</div>
                    <h3>{product.name}</h3>
                    <p>{product.description}</p>
                    <div className="product-footer">
                      <strong>{money(product.priceInCent)}</strong>
                      <button disabled={busyId === product.id} onClick={() => addToCart(product.id)}>
                        {busyId === product.id ? 'Adding…' : 'Add to cart'}
                      </button>
                    </div>
                  </div>
                </article>
              ))}
            </div>
            {!visibleProducts.length && <p className="empty">No components match this filter.</p>}
          </section>

          <aside className="cart-card">
            <div className="cart-heading"><div><p className="section-kicker">YOUR BUILD</p><h2>Shopping cart</h2></div><span>{cartLines.length}</span></div>
            <div className="cart-lines">
              {cartLines.map(({ product, quantity }) => (
                <div className="cart-line" key={product.id}>
                  <div><strong>{product.name}</strong><span>Qty {quantity}</span></div>
                  <b>{money(product.priceInCent * quantity)}</b>
                </div>
              ))}
              {!cartLines.length && <div className="cart-empty"><span>+</span><p>Your build is empty.<br />Add a component to begin.</p></div>}
            </div>
            <div className="totals">
              <div><span>Delivery</span><span>{money(cart?.deliveryCostInCent ?? 0)}</span></div>
              <div className="grand-total"><span>Total</span><strong>{money(cart?.totalCostInCent ?? 0)}</strong></div>
            </div>
            <button className="checkout" disabled={!cartLines.length || busyId === 'checkout'} onClick={checkout}>
              {busyId === 'checkout' ? 'Confirming…' : 'Confirm order'}
            </button>
            <p className="demo-note">Demo checkout · No payment is collected</p>
          </aside>
        </div>
      </main>

      <footer><span>Java · Spring Boot · React · TypeScript · MySQL</span><span>Full-stack portfolio project</span></footer>
    </div>
  )
}

export default App
