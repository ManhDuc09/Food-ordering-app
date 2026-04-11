import { createRouter, createWebHistory } from 'vue-router'

import Home from '../pages/Home.vue'
import Menu from '../pages/Menu.vue'

import ProductDetails from '../pages/ProductDetails.vue'
import Login from '../pages/Login.vue'
import Map from '../pages/Map.vue'
const routes = [
  { path: '/', component: Home },
  { path: '/menu', component: Menu },
  { 
    path: '/product/:id', 
    name: 'ProductDetails', 
    component: ProductDetails 
  },
  {path: '/login' , component: Login},
  {path: '/map', component: Map}
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  }
})

export default router