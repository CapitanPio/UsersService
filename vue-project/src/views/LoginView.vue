<script setup lang="ts">

import {login} from '@/api/users_service_queries'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')

const router = useRouter()

const handleLogin = async () => {
  const response = await login(username.value, password.value)
  if (response.status === 200) {
    const data = response.data
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', username.value)
    console.log('Login successful. Token:', data.token)
    router.push('/usersManager')
  } else {
    alert('Login failed')
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

        <div class="modal-footer" style="justify-content: center">
          <button type="button" class="btn btn-primary" @click="handleLogin">Log in</button>
        </div>
      </div>
    </div>
  </div>
</template>
