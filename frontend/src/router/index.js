import { createRouter, createWebHistory } from 'vue-router'

import Home from '../pages/Home.vue'
import Menu from '../pages/Menu.vue'
import ProductDetails from '../pages/ProductDetails.vue'
import Login from '../pages/Login.vue'
import Map from '../pages/Map.vue'
import Cart from '../pages/Cart.vue'
import Register from '../pages/Register.vue'
import Payment from '../pages/Payment.vue'
import Profile from '../pages/Profile.vue'
import Dashboard from '../pages/Dashboard.vue'
import { authState, syncAuth, checkAndHandleExpiry } from '../store/auth'

const routes = [
  { path: '/', component: Home },
  { path: '/menu', component: Menu },
  { path: '/product/:id', name: 'ProductDetails', component: ProductDetails },
  { path: '/cart', component: Cart },
  { path: '/payment', component: Payment, meta: { requiresAuth: true } },
  { path: '/profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/map', component: Map },
  { path: '/dashboard', component: Dashboard, meta: { requiresAuth: true, role: 'branch_manager', layout: 'dashboard' } }
]

syncAuth()

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  syncAuth()
  const expired = checkAndHandleExpiry()

  if (expired) {
    
    if (from.matched.length === 0) {
      authState.sessionExpired = false
      return next({ path: '/login' })
    }
    
    return next(false)
  }

  if (to.meta.requiresAuth && !authState.isLoggedIn) {
    return next({ path: '/login', query: { redirect: to.fullPath } })
  }

  if (to.meta.role && authState.user?.role !== to.meta.role) {
    return next({ path: '/' })
  }

  next()
})

export default router
