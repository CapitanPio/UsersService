<script setup lang="ts">

import { getUsers, updateUser } from '@/api/users_service_queries'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/')
}

</script>

<template>

  <div class="modal-footer" style="justify-content: center; flex-direction: column; align-items: center">
<h1>Home</h1>
    <button type="button" class="btn btn-primary" @click="handleLogout">Log out</button>
    <button type="button" class="btn btn-primary" @click="() => router.push('/usersManager')">Users manager</button>
    <button type="button" class="btn btn-primary" @click="() => router.push('/rolesManager')">Roles manager</button>
    <button type="button" class="btn btn-primary" @click="() => router.push('/permissionsManager')">Permissions manager</button>
    <button type="button" class="btn btn-primary" @click="() => router.push('/permissionsAndRolesTester')">Permissions and Roles Tester</button>

  </div>
</template>

<style scoped>

button {
  margin-top: 10px;
}

</style>
