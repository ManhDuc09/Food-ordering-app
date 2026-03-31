import { createRouter, createWebHistory } from 'vue-router'

import Home from '../pages/Home.vue'
import Menu from '../pages/Menu.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/menu', component: Menu },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router