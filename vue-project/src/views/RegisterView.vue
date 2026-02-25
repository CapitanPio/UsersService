<script setup lang="ts">

import {register} from '@/api/users_service_queries'
import { ref } from 'vue'


const registerError = ref('')
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const email = ref('')
const handleRegister = async () => {
  try{
    registerError.value = ''
    if (!username.value || !password.value || !email.value) {
      registerError.value = 'Please fill in all fields'
      return
    }

    if (password.value !== confirmPassword.value) {
      registerError.value = 'Passwords do not match'
      return
    }

    else{
      const response = await register(username.value, email.value, password.value)
      if (response.status === 200 || response.status === 201) {
        localStorage.setItem('token', response.data.token)
        window.location.href = '/'
      }
      else {
        registerError.value = 'Registration failed'
      }
    }
  }catch (e){
        if (!e.response && e.request) {
      registerError.value = 'Service unavailable. Please try again later.'
    } else {
      const data = e.response?.data
      if (data && typeof data === 'object') {
        registerError.value = Object.values(data).join(' | ')
      } else {
        registerError.value = data?.error || data || 'Unknown error'
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

        <div class="modal-footer" style="justify-content: center; flex-direction: column; align-items: center">
          <button type="button" class="btn btn-primary" @click="handleRegister">Register</button>
          <div v-if="registerError" class="alert alert-danger" role="alert" style="margin-top: 10px; text-align: center">
            {{ registerError }}
          </div>
        </div>

      </div>
    </div>
  </div>
</template>
