import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const username = ref<string | null>(localStorage.getItem('username'))

  async function login(usernameInput: string, password: string) {
    const res = await loginApi({ username: usernameInput, password })
    token.value = res.token
    username.value = res.username
    localStorage.setItem('token', res.token)
    localStorage.setItem('username', res.username)
  }

  function logout() {
    token.value = null
    username.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('username')
  }

  return { token, username, login, logout }
})
