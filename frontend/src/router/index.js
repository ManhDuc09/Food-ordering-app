import { createRouter, createWebHistory } from 'vue-router'

import Home from '../pages/Home.vue'
import Menu from '../pages/Menu.vue'

import ProductDetails from '../pages/ProductDetails.vue'
import Login from '../pages/Login.vue'
import Map from '../pages/Map.vue'
import Cart from '../pages/Cart.vue'
import Register from '../pages/Register.vue'
import Payment from '../pages/Payment.vue'
import { authState, syncAuth } from '../store/auth'


const routes = [
  { path: '/', component: Home },
  { path: '/menu', component: Menu },
  { 
    path: '/product/:id', 
    name: 'ProductDetails', 
    component: ProductDetails 
  },
  { path: '/cart', component: Cart },
  { path: '/payment', component: Payment, meta: { requiresAuth: true } },
  { path: '/login' , component: Login},
  { path: '/register', component: Register },
  { path: '/map', component: Map}
]

syncAuth()

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  syncAuth()
  if (to.meta.requiresAuth && !authState.isLoggedIn) {
    return next({ path: '/login', query: { redirect: to.fullPath } })
  }
  next()
})

export default router