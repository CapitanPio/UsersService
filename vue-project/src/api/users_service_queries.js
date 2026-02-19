import api from './axios'

export const login = (username, password) =>
    api.post('/api/auth/login', { username, password })

export const register = (username, email, password) =>
    api.post('/api/auth/register', { username, email, password })

export const changePassword = (oldPassword, newPassword) =>
    api.post('/api/auth/change-password', { oldPassword, newPassword })
