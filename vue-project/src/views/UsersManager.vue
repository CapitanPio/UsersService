<script setup lang="ts">

import { getUsers, updateUser, register, deleteUser, getAllRoles } from '@/api/users_service_queries'
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

const openRegisterModal = () => {
  const registerModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('registerModal')!)
  registerModal.show()
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

const usernameRegister = ref('')
const passwordRegister = ref('')
const confirmPasswordRegister = ref('')
const emailRegister = ref('')
const roleIdRegister = ref<number | null>(null)
const registerErr = ref('')
const roles = ref<any[]>([])

const handleRegister = async () => {
  if (passwordRegister.value !== confirmPasswordRegister.value) {
    registerErr.value = 'Passwords do not match'
    return
  }
  try {
    await register(usernameRegister.value, emailRegister.value, passwordRegister.value, roleIdRegister.value)
    registerErr.value = ''
    bootstrap.Modal.getOrCreateInstance(document.getElementById('registerModal')!).hide()
    fetchUsers()
  } catch (error: any) {
    const data = error.response?.data
    registerErr.value = typeof data === 'object' ? Object.values(data).join(' | ') : (data || 'Registration failed')
  }
}

const handleDeleteUser = async (userId: number) => {
  try {
    await deleteUser(userId)
    fetchUsers()
  } catch (error) {
    alert('Failed to delete user')
  }
}


onMounted(async () => {
  await fetchUsers()
  try {
    const response = await getAllRoles()
    roles.value = response.data
  } catch {}
})


</script>

<template>
  <div class="d-flex justify-content-between align-items-center px-3 py-2" style="border-bottom: 3px solid #000000">
    <h1 class="mb-0">Users Manager</h1>
    <div>
      <button class="btn btn-secondary" @click="openRegisterModal" style="margin-right: 5px;">Register User</button>
      <button class="btn btn-secondary" @click="$router.push('/home')">Home</button>
    </div>


  </div>

  <div class="px-3 pt-3">
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Username</th>
          <th scope="col">Email</th>
          <th scope="col">Role</th>
          <th scope="col">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>
            <button type="button" class="btn btn-primary btn-sm" @click="openEditModal(user)">Edit</button>
            <button type="button" class="btn btn-danger btn-sm" @click="handleDeleteUser(user.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>


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
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="saveChanges">Confirm changes</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>


  <div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="registerModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="registerModalLabel">Register User</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input
            type="text"
            class="form-control"
            placeholder="Username"
            aria-label="Username"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
            v-model="usernameRegister"
          />
          <input
            type="text"
            class="form-control"
            placeholder="Email"
            aria-label="Email"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
            v-model="emailRegister"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Password"
            aria-label="Password"
            v-model="passwordRegister"
            aria-describedby="basic-addon2"
            style="margin-bottom: 10px"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Confirm Password"
            aria-label="Confirm Password"
            v-model="confirmPasswordRegister"
            aria-describedby="basic-addon3"
            style="margin-bottom: 10px"
          />
          <select class="form-select" v-if="roles.length > 0" v-model="roleIdRegister" style="margin-bottom: 10px">
            <option :value="null">No role</option>
            <option v-for="role in roles" :key="role.id" :value="role.id">{{ role.name }}</option>
          </select>
          <div v-if="registerErr" class="alert alert-danger mb-0">{{ registerErr }}</div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="handleRegister">Register</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
