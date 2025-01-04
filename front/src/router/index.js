import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/gravarVideo',
    name: 'CaptureMedia',
    component: () => import('@/components/CaptureMedia.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/dados-casamento',
    name: 'dados-casamento',
    component: () => import('@/components/FormCasamento.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/auth/signin',
    name: 'Login',
    component: () => import('@/components/Login.vue'),
    meta: { requiresNoAuth: true },
  },
  {
    path: '/auth/signup',
    name: 'Cadastro',
    component: () => import('@/components/CadastroConvidado.vue'),
    meta: { requiresNoAuth: true },
  },
  {
    path: '/auth/noivos/signup',
    name: 'CadastroNoivos',
    component: () => import('@/components/CadastroNoivas.vue'),
    meta: { requiresNoAuth: true },
  },
  {
    path: '/menu',
    name: 'Menu',
    component: () => import('@/views/Menu.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/mural',
    name: 'mural',
    component: () => import('@/components/Mural.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/casamento',
    name: 'casamento',
    component: () => import('@/views/Casamento.vue'),
    meta: { requiresAuth: false },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes, // Defining routes
});

// Global navigation guard
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken'); // Retrieve token from localStorage

  // Capture the UUID from query string
  const uuid = to.query.uuid;
  console.log('UUID captured:', uuid);

  // If the route requires authentication and no token exists
  if (to.meta.requiresAuth && !token) {
    next({
      path: '/auth/signup',
      query: { uuid }, // Pass the UUID or other parameters if necessary
    });
  }
  // If the route requires no authentication but the user is already logged in
  else if (to.meta.requiresNoAuth && token) {
    next({
      path: '/menu',
      query: { uuid }, // Pass the UUID to redirect to the menu
    });
  } else {
    next(); // Otherwise, continue navigation
  }
});

export default router;
