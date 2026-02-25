<script setup lang="ts">

import { getAllPermissions, getPermission, updatePermission, createPermission, deletePermission } from '@/api/users_service_queries'
import { ref, onMounted } from 'vue'
import * as bootstrap from 'bootstrap'
import { error } from 'console'


const modalErr = ref('')
const permissions = ref<any[]>([])
const selectedPermission = ref({
  id: null,
  name: '',
})

const openCreatePermissionModal = () => {
  permissionNameRegister.value = ''
  registerError.value = ''
  const createPermissionModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('createPermissionModal')!)
  createPermissionModal.show()
}

const openEditModal = (permission: any) => {
  selectedPermission.value = { ...permission }
  modalErr.value = ''
  const editModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('editModal')!)
  editModal.show()
}

const fetchPermissions = async () => {
  try {
    const response = await getAllPermissions()
    permissions.value = response.data
  } catch (error) {
    alert('Failed to fetch permissions')
  }
}

const saveChanges = async () => {
  try {
    await updatePermission(selectedPermission.value.id, selectedPermission.value.name)
    modalErr.value = ''
    bootstrap.Modal.getOrCreateInstance(document.getElementById('editModal')!).hide()
    fetchPermissions()
  } catch (error: any) {
    modalErr.value = error.response?.data?.error || error.response?.data || 'Unknown error'
  }
}
const registerError = ref('')
const permissionNameRegister = ref('')
const handleCreatePermission = async () => {
  try{
    registerError.value = ''
    if (!permissionNameRegister.value) {
      registerError.value = 'Please fill in the permission name'
      return
    }
    const response = await createPermission({ name: permissionNameRegister.value })
    if (response.status === 200 || response.status === 201) {
      const createPermissionModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('createPermissionModal')!)
      createPermissionModal.hide()
      permissionNameRegister.value = ''
      fetchPermissions()

    }
    else {
      registerError.value = 'Creation failed'
    }
  } catch (e){
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

const handleDeletePermission = async (permissionId: number) => {
  try {
    await deletePermission(permissionId)
    fetchPermissions()
  } catch (error) {
    alert('Failed to delete permission')
  }
}


onMounted(fetchPermissions)


</script>

<template>
  <div class="d-flex justify-content-between align-items-center px-3 py-2" style="border-bottom: 3px solid #000000">
    <h1 class="mb-0">Permissions Manager</h1>
    <div>
      <button class="btn btn-secondary" @click="openCreatePermissionModal" style="margin-right: 5px;">Create Permission</button>
      <button class="btn btn-secondary" @click="$router.push('/home')">Home</button>
    </div>


  </div>

  <div class="px-3 pt-3">
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Permission</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="permission in permissions" :key="permission.id">
          <td>{{ permission.name }}</td>
          <td>
            <button type="button" class="btn btn-primary btn-sm" @click="openEditModal(permission)">Edit</button>
            <button type="button" class="btn btn-danger btn-sm" @click="handleDeletePermission(permission.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>


  <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editModalLabel">Edit permission</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">

          <input type="text" class="form-control mb-2" placeholder="Permission Name" v-model="selectedPermission.name">
          <div v-if="modalErr" class="alert alert-danger">{{ modalErr }}</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="saveChanges">Confirm changes</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>


  <div class="modal fade" id="createPermissionModal" tabindex="-1" role="dialog" aria-labelledby="createPermissionModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="createPermissionModalLabel">Create Permission</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input
            type="text"
            class="form-control"
            placeholder="Permission Name"
            aria-label="Permission Name"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
            v-model="permissionNameRegister"
          />
          <div v-if="registerError" class="alert alert-danger">{{ registerError }}</div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="handleCreatePermission">Create</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
