import api from './axios'

// Auth endpoints

export const login = (username, password) =>
    api.post('/api/auth/login', { username, password })


// User management endpoints

export const register = (username, email, password, roleId = null) =>
    api.post('/api/auth/register', { username, email, password, ...(roleId !== null && { roleId }) })

export const changePassword = (oldPassword, newPassword) =>
    api.post('/api/auth/change-password', { oldPassword, newPassword })

export const getUsers = () =>
    api.get('/api/auth/users')

export const updateUser = (userId, userData) =>
    api.put(`/api/auth/update/${userId}`, userData)

export const deleteUser = (userId) =>
    api.delete(`/api/auth/delete/${userId}`)

// Permissions management

export const getAllPermissions = () =>
    api.get('/api/auth/permissions')

export const getPermission = (permissionId) =>
    api.get(`/api/auth/permissions/${permissionId}`)

export const createPermission = (permissionData) =>
    api.post('/api/auth/permissions', permissionData)

export const updatePermission = (permissionId, newName) =>
    api.put(`/api/auth/permissions/${permissionId}`, { newName })

export const deletePermission = (permissionId) =>
    api.delete(`/api/auth/permissions/${permissionId}`)

// Roles management

export const getAllRoles = () =>
    api.get('/api/auth/roles')

export const getRole = (roleName) =>
    api.get(`/api/auth/roles/${roleName}`)

export const createRole = (roleData) =>
    api.post('/api/auth/roles', roleData)

export const updateRole = (roleData) =>
    api.put('/api/auth/roles', roleData)

export const deleteRole = (roleName) =>
    api.delete(`/api/auth/roles/${roleName}`)

export const checkAccess = (role, permissions) =>
    api.post('/api/auth/check-access', { role, permissions })
