import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import HomeView from '../views/Home.vue'
import UsersManager from '../views/UsersManager.vue'
import PermissionsManager from '../views/PermissionsManager.vue'
import rolesManager from '../views/RolesManager.vue'
import permissionsAndRolesTester from '../views/PermissionsAndRolesTester.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/usersManager',
      name: 'usersManager',
      component: UsersManager,
    },
    {
      path: '/permissionsManager',
      name: 'permissionsManager',
      component: PermissionsManager,
    },
      {
      path: '/rolesManager',
      name: 'rolesManager',
      component: rolesManager,
    },
    {
      path: '/permissionsAndRolesTester',
      name: 'permissionsAndRolesTester',
      component: permissionsAndRolesTester,
    },
  ],
})

export default router
