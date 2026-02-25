<script setup lang="ts">

import {login} from '@/api/users_service_queries'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const error = ref('')
const username = ref('')
const password = ref('')

const router = useRouter()

const handleLogin = async () => {
  try{

    error.value = ''
    if (!username.value || !password.value) {
      error.value = 'Please enter both username and password'
      return

    }
    else{
      const response = await login(username.value, password.value)
      const data = response.data
      localStorage.setItem('token', data.token)
      localStorage.setItem('username', username.value)
      console.log('Login successful. Token:', data.token)
      router.push('/home')
    }
  }catch (e){
    if (!e.response && e.request) {
      error.value = 'Service unavailable. Please try again later.'
    } else {
      const data = e.response?.data
      if (data && typeof data === 'object') {
        error.value = Object.values(data).join(' | ')
      } else {
        error.value = data?.error || data || 'Unknown error'
      }
    }
  }
}

</script>

<template>
  <div class="modal show d-block" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header" style="justify-content: center">
          <h5 class="modal-title">Login</h5>
        </div>
        <div class="modal-body">
          <input
            type="text"
            class="form-control"
            placeholder="Username"
            v-model="username"
            aria-label="Username"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Password"
            v-model="password"
            aria-label="Password"
            aria-describedby="basic-addon2"
            style="margin-bottom: 10px"
          />
          <div style="text-align: center">
            <a href="/register">Register</a>
          </div>
        </div>

        <div class="modal-footer" style="justify-content: center; flex-direction: column; align-items: center">
          <button type="button" class="btn btn-primary" @click="handleLogin">Log in</button>
          <div v-if="error" class="alert alert-danger mt-2 mb-0">{{ error }}</div>
        </div>

      </div>
    </div>
  </div>
</template>
