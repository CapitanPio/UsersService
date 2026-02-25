<script setup lang="ts">

import { checkAccess, getAllPermissions, getAllRoles } from '@/api/users_service_queries'
import { ref, onMounted } from 'vue'
import * as bootstrap from 'bootstrap'

const tests = ref<any[]>([])
const availablePermissions = ref<any[]>([])
const availableRoles = ref<any[]>([])
const testTypeRegister = ref('')
const selectedPermissions = ref<string[]>([])
const selectedRole = ref('')
const registerError = ref('')

const fetchData = async () => {
  const [permsRes, rolesRes] = await Promise.all([getAllPermissions(), getAllRoles()])
  availablePermissions.value = permsRes.data
  availableRoles.value = rolesRes.data
}

onMounted(fetchData)

const openCreateTestModal = () => {
  testTypeRegister.value = ''
  selectedPermissions.value = []
  selectedRole.value = ''
  registerError.value = ''
  bootstrap.Modal.getOrCreateInstance(document.getElementById('createTestModal')!).show()
}

const handleCreateTest = () => {
  registerError.value = ''
  if (!testTypeRegister.value) {
    registerError.value = 'Please select a type'
    return
  }
  if (testTypeRegister.value === 'permission' && selectedPermissions.value.length === 0) {
    registerError.value = 'Please select at least one permission'
    return
  }
  if (testTypeRegister.value === 'role' && !selectedRole.value) {
    registerError.value = 'Please select a role'
    return
  }
  if (testTypeRegister.value === 'permission') {
    tests.value.push({ type: 'permission', names: [...selectedPermissions.value], isUserAllowed: 'unknown' })
  } else {
    tests.value.push({ type: 'role', name: selectedRole.value, isUserAllowed: 'unknown' })
  }
  bootstrap.Modal.getOrCreateInstance(document.getElementById('createTestModal')!).hide()
}

const checkTest = async (test: any) => {
  try {
    let response
    if (test.type === 'permission') {
      response = await checkAccess(null, test.names)
    } else {
      response = await checkAccess(test.name, null)
    }
    test.isUserAllowed = response.data.hasAccess
  } catch (e: any) {
    test.isUserAllowed = 'error: ' + (e.response?.data?.error || 'unknown')
  }
}

</script>

<template>

  <div class="d-flex justify-content-between align-items-center px-3 py-2" style="border-bottom: 3px solid #000000">
    <h1 class="mb-0">Permissions & Roles Tester</h1>
    <div>
      <button class="btn btn-secondary" @click="openCreateTestModal" style="margin-right: 5px;">Create Test</button>
      <button class="btn btn-secondary" @click="$router.push('/home')">Home</button>
    </div>
  </div>

  <div class="px-3 pt-3">
    <div v-if="tests.length === 0" class="text-muted">No tests yet. Create one above.</div>
    <div
      v-for="(test, index) in tests"
      :key="index"
      style="margin-top: 10px; border: 1px solid #dee2e6; border-radius: 4px; padding: 10px; width: 350px;"
    >
      <p class="mb-1"><strong>Type:</strong> {{ test.type.toUpperCase() }}</p>
      <p class="mb-1" v-if="test.type === 'permission'"><strong>Permissions:</strong> {{ test.names.join(', ') }}</p>
      <p class="mb-1" v-if="test.type === 'role'"><strong>Role:</strong> {{ test.name }}</p>
      <p class="mb-2"><strong>Has access?</strong> {{ test.isUserAllowed }}</p>
      <button class="btn btn-primary btn-sm" @click="checkTest(test)">Check</button>
    </div>
  </div>


  <div class="modal fade" id="createTestModal" tabindex="-1" role="dialog" aria-labelledby="createTestModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="createTestModalLabel">Create Test</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">

          <select class="form-select" v-model="testTypeRegister" style="margin-bottom: 10px;">
            <option value="" disabled>Select type</option>
            <option value="permission">Permission</option>
            <option value="role">Role</option>
          </select>

          <div v-if="testTypeRegister === 'permission'" style="max-height: 200px; overflow-y: auto; border: 1px solid #dee2e6; border-radius: 4px; padding: 8px;">
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

          <div v-if="testTypeRegister === 'role'" style="max-height: 200px; overflow-y: auto; border: 1px solid #dee2e6; border-radius: 4px; padding: 8px;">
            <div v-if="availableRoles.length === 0" class="text-muted small">No roles available</div>
            <div v-for="role in availableRoles" :key="role.id" class="form-check">
              <input
                class="form-check-input"
                type="radio"
                :id="'role-' + role.id"
                :value="role.name"
                v-model="selectedRole"
              >
              <label class="form-check-label" :for="'role-' + role.id">
                {{ role.name }}
              </label>
            </div>
          </div>

          <div v-if="registerError" class="alert alert-danger mt-2">{{ registerError }}</div>

        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="margin-right: 10px;" @click="handleCreateTest">Create</button>
          <button type="button" class="btn btn-secondary" style="margin-left: 10px;" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>

</style>
