<script setup lang="ts">

import { getAllRoles, getAllPermissions, updateRole, createRole, deleteRole } from '@/api/users_service_queries'
import { ref, onMounted } from 'vue'
import * as bootstrap from 'bootstrap'


const modalErr = ref('')
const roles = ref<any[]>([])
const availablePermissions = ref<any[]>([])
const selectedRole = ref({ id: null, name: '', permissions: [] as any[] })
const originalRoleName = ref('')
const originalPermissions = ref<string[]>([])
const selectedPermissions = ref<string[]>([])

const openCreateRoleModal = () => {
  roleNameRegister.value = ''
  registerError.value = ''
  const createRoleModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('createRoleModal')!)
  createRoleModal.show()
}

const openEditModal = (role: any) => {
  selectedRole.value = { ...role }
  originalRoleName.value = role.name
  originalPermissions.value = role.permissions.map((p: any) => p.name)
  selectedPermissions.value = [...originalPermissions.value]
  modalErr.value = ''
  const editModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('editModal')!)
  editModal.show()
}

const fetchRoles = async () => {
  try {
    const response = await getAllRoles()
    roles.value = response.data
  } catch (error) {
    alert('Failed to fetch roles')
  }
}

const fetchPermissions = async () => {
  try {
    const response = await getAllPermissions()
    availablePermissions.value = response.data
  } catch (error) {
    alert('Failed to fetch permissions')
  }
}

const saveChanges = async () => {
  try {
    const toAdd = selectedPermissions.value.filter(n => !originalPermissions.value.includes(n))
    const toRemove = originalPermissions.value.filter(n => !selectedPermissions.value.includes(n))
    await updateRole({
      name: originalRoleName.value,
      newName: selectedRole.value.name !== originalRoleName.value ? selectedRole.value.name : null,
      permissionsToAdd: toAdd,
      permissionsToRemove: toRemove,
    })
    modalErr.value = ''
    bootstrap.Modal.getOrCreateInstance(document.getElementById('editModal')!).hide()
    fetchRoles()
  } catch (error: any) {
    modalErr.value = error.response?.data?.error || error.response?.data || 'Unknown error'
  }
}

const registerError = ref('')
const roleNameRegister = ref('')
const handleCreateRole = async () => {
  try {
    registerError.value = ''
    if (!roleNameRegister.value) {
      registerError.value = 'Please fill in the role name'
      return
    }
    const response = await createRole({ name: roleNameRegister.value })
    if (response.status === 200 || response.status === 201) {
      bootstrap.Modal.getOrCreateInstance(document.getElementById('createRoleModal')!).hide()
      roleNameRegister.value = ''
      fetchRoles()
    } else {
      registerError.value = 'Creation failed'
    }
  } catch (e: any) {
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

const handleDeleteRole = async (roleName: string) => {
  try {
    await deleteRole(roleName)
    fetchRoles()
  } catch (error) {
    alert('Failed to delete role')
  }
}

onMounted(() => {
  fetchRoles()
  fetchPermissions()
})

</script>

<template>
  <div class="d-flex justify-content-between align-items-center px-3 py-2" style="border-bottom: 3px solid #000000">
    <h1 class="mb-0">Roles Manager</h1>
    <div>
      <button class="btn btn-secondary" @click="openCreateRoleModal" style="margin-right: 5px;">Create Role</button>
      <button class="btn btn-secondary" @click="$router.push('/home')">Home</button>
    </div>
  </div>

  <div class="px-3 pt-3">
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Role</th>
          <th scope="col">Permissions</th>
          <th scope="col">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="role in roles" :key="role.id">
          <td>{{ role.name }}</td>
          <td>{{ role.permissions.map((p: any) => p.name).join(', ') }}</td>
          <td>
            <button type="button" class="btn btn-primary btn-sm me-1" @click="openEditModal(role)">Edit</button>
            <button type="button" class="btn btn-danger btn-sm" @click="handleDeleteRole(role.name)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>


  <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editModalLabel">Edit Role</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input type="text" class="form-control mb-3" placeholder="Role Name" v-model="selectedRole.name">
          <label class="form-label">Permissions</label>
          <div style="max-height: 200px; overflow-y: auto; border: 1px solid #dee2e6; border-radius: 4px; padding: 8px;">
            <div v-if="availablePermissions.length === 0" class="text-muted small">No permissions available</div>
            <div v-for="permission in availablePermissions" :key="permission.id" class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                :id="'perm-' + permission.id"
                :value="permission.name"
                v-model="selectedPermissions"
              >
              <label class="form-check-label" :for="'perm-' + permission.id">
                {{ permission.name }}
              </label>
            </div>
          </div>
          <div v-if="modalErr" class="alert alert-danger mt-2 mb-0">{{ modalErr }}</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="saveChanges">Confirm changes</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>


  <div class="modal fade" id="createRoleModal" tabindex="-1" role="dialog" aria-labelledby="createRoleModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="createRoleModalLabel">Create Role</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input
            type="text"
            class="form-control"
            placeholder="Role Name"
            style="margin-bottom: 10px"
            v-model="roleNameRegister"
          />
          <div v-if="registerError" class="alert alert-danger">{{ registerError }}</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="handleCreateRole">Create</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
