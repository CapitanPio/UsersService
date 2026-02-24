import api from './axios'

export const login = (username, password) =>
    api.post('/api/auth/login', { username, password })

export const register = (username, email, password) =>
    api.post('/api/auth/register', { username, email, password })

export const changePassword = (oldPassword, newPassword) =>
    api.post('/api/auth/change-password', { oldPassword, newPassword })

export const getUsers = () =>
    api.get('/api/auth/users')

export const updateUser = (userId, userData) =>
    api.put(`/api/auth/update/${userId}`, userData)
