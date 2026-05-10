import { reactive } from 'vue'


export const authState = reactive({
  isLoggedIn: !!localStorage.getItem('token'),
  user: JSON.parse(localStorage.getItem('user') || 'null')
})

export const syncAuth = () => {
  authState.isLoggedIn = !!localStorage.getItem('token')
  authState.user = JSON.parse(localStorage.getItem('user') || 'null')
}