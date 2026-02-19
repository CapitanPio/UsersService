<script setup lang="ts">

import {register} from '@/api/users_service_queries'
import { ref } from 'vue'

const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const email = ref('')
const handleRegister = async () => {
  if (password.value !== confirmPassword.value) {
    alert('Passwords do not match')
    return
  }
  const response = await register(username.value, email.value, password.value)
  if (response.status === 200) {
    const data = await response.json()
    localStorage.setItem('token', data.token)
    window.location.href = '/'
  } else {
    alert('Registration failed')
  }
}

</script>

<template>
  <div class="modal show d-block" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header" style="justify-content: center">
          <h5 class="modal-title">Register</h5>
        </div>
        <div class="modal-body">
          <input
            type="text"
            class="form-control"
            placeholder="Username"
            aria-label="Username"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
            v-model="username"
          />
          <input
            type="text"
            class="form-control"
            placeholder="Email"
            aria-label="Email"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
            v-model="email"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Password"
            aria-label="Password"
            v-model="password"
            aria-describedby="basic-addon2"
            style="margin-bottom: 10px"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Confirm Password"
            aria-label="Confirm Password"
            v-model="confirmPassword"
            aria-describedby="basic-addon3"
            style="margin-bottom: 10px"
          />
          <div style="text-align: center">
            <a href="/">Login</a>
          </div>
        </div>

        <div class="modal-footer" style="justify-content: center">
          <button type="button" class="btn btn-primary" @click="handleRegister">Register</button>
        </div>
      </div>
    </div>
  </div>
</template>
