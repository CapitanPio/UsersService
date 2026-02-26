import api from './axios'

// Auth endpoints

export const login = (username, password) =>
    api.post('/api/auth/login', { username, password })


// User management endpoints

export const register = (username, email, password, roleId = null) =>
    api.post('/api/auth/register', { username, email, password, ...(roleId !== null && { roleId }) })

export const changePassword = (oldPassword, newPassword) =>
    api.post('/api/auth/users/change-password', { oldPassword, newPassword })

export const getUsers = () =>
    api.get('/api/users')

export const updateUser = (userId, userData) =>
    api.put(`/api/users/update/${userId}`, userData)

export const deleteUser = (userId) =>
    api.delete(`/api/users/delete/${userId}`)

// Permissions management

export const getAllPermissions = () =>
    api.get('/api/permissions')

export const getPermission = (permissionName) =>
    api.get(`/api/permissions/${permissionName}`)

export const createPermission = (permissionData) =>
    api.post('/api/permissions', permissionData)

export const updatePermission = (permissionId, newName) =>
    api.put(`/api/permissions/${permissionId}`, { newName })

export const deletePermission = (permissionId) =>
    api.delete(`/api/permissions/${permissionId}`)

// Roles management

export const getAllRoles = () =>
    api.get('/api/roles')

export const getRole = (roleName) =>
    api.get(`/api/roles/${roleName}`)

export const createRole = (roleData) =>
    api.post('/api/roles', roleData)

export const updateRole = (roleData) =>
    api.put('/api/roles', roleData)

export const deleteRole = (roleName) =>
    api.delete(`/api/roles/${roleName}`)

export const checkAccess = (role, permissions) =>
    api.post('/api/check-access', { role, permissions })
