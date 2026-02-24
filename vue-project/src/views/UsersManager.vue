<script setup lang="ts">

import { getUsers, updateUser } from '@/api/users_service_queries'
import { ref, onMounted } from 'vue'
import * as bootstrap from 'bootstrap'


const modalErr = ref('')
const originalUsername = ref('')
const users = ref<any>([])
const selectedUser = ref({
  id: null,
  username: '',
  email: '',
  currentPassword: '',
  newPassword: '',
})

const openEditModal = (user: any) => {
  selectedUser.value = { ...user }
  originalUsername.value = user.username
  const editModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('editModal')!)
  editModal.show()
}

const fetchUsers = async () => {
  try {
    const response = await getUsers()
    users.value = response.data
  } catch (error) {
    alert('Failed to fetch users')
  }
}

const saveChanges = async () => {
  try {
    const response = await updateUser(selectedUser.value.id, {
      username: selectedUser.value.username,
      email: selectedUser.value.email,
      oldPassword: selectedUser.value.currentPassword,
      newPassword: selectedUser.value.newPassword,
    })
    if (originalUsername.value === localStorage.getItem('username')) {
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('username', selectedUser.value.username)
    }
    modalErr.value = ''
    fetchUsers()
  } catch (error: any) {
    modalErr.value = error.response?.data?.error || error.response?.data || 'Unknown error'
  }
}


onMounted(fetchUsers)


</script>

<template>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">Id</th>
        <th scope="col">Username</th>
        <th scope="col">Email</th>
        <th scope="col">Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>{{ user.id }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>
          <button type="button" class="btn btn-primary btn-sm" @click="openEditModal(user)">Edit</button>
          <button type="button" class="btn btn-danger btn-sm">Delete</button>
        </td>
      </tr>
    </tbody>
  </table>

  <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editModalLabel">Edit user</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">

          <input type="text" class="form-control mb-2" placeholder="Username" v-model="selectedUser.username">
          <input type="email" class="form-control mb-2" placeholder="Email" v-model="selectedUser.email">
          <input type="password" class="form-control mb-2" placeholder="Current Password" v-model="selectedUser.currentPassword">
          <input type="password" class="form-control mb-2" placeholder="New Password" v-model="selectedUser.newPassword">
          <div v-if="modalErr" class="alert alert-danger">{{ modalErr }}</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" @click="saveChanges">Confirm changes</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
