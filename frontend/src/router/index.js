import { createRouter, createWebHistory } from 'vue-router'

import Home from '../pages/Home.vue'
import Menu from '../pages/Menu.vue'

import ProductDetails from '../pages/ProductDetails.vue'
import Login from '../pages/Login.vue'
import Map from '../pages/Map.vue'
import Cart from '../pages/Cart.vue'
import Register from '../pages/Register.vue'


const routes = [
  { path: '/', component: Home },
  { path: '/menu', component: Menu },
  { 
    path: '/product/:id', 
    name: 'ProductDetails', 
    component: ProductDetails 
  },
  { path: '/cart', component: Cart },
  { path: '/login' , component: Login},
  { path: '/register', component: Register },
  { path: '/map', component: Map}
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  }
})

export default router