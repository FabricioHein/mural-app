import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { requiresAuth: false }, // Protege a rota
  },
  {
    path: '/gravarVideo',
    name: 'CaptureMedia',
    component: () => import('@/components/CaptureMedia.vue'),
    meta: { requiresAuth: true }, // Protege a rota
  },
  {
    path: '/dados-casamento',
    name: 'dados-casamento',
    component: () => import('@/components/FormCasamento.vue'),
    meta: { requiresAuth: true }, // Protege a rota
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
    meta: { requiresAuth: true }, // Protege a rota
  },
  {
    path: '/mural',
    name: 'mural',
    component: () => import('@/components/Mural.vue'),
    meta: { requiresAuth: true }, // Protege a rota
  },
  {
    path: '/casamento',
    name: 'casamento',
    component: () => import('@/views/Casamento.vue'),
    meta: { requiresNoAuth: false }, // Protege se já estiver logado
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes, // Definindo as rotas
});

// Guarda de navegação global
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken'); // Obtém o token do localStorage

  // Captura o UUID da query string
  const uuid = to.query.uuid;
  console.log('UUID capturado:', uuid);

  // Verifica se a rota requer autenticação e se o usuário não está logado
  if (to.meta.requiresAuth && !token) {
    next({
      path: '/auth/signup',
      query: { uuid }, // Repassa o UUID ou outro parâmetro se necessário
    });
  }
  // Verifica se a rota exige que o usuário não esteja logado (para impedir o acesso a cadastros)
  else if (to.meta.requiresNoAuth && token) {
    next({
      path: '/menu',
      query: { uuid }, // Repassa o UUID ou outro parâmetro ao redirecionar para o menu
    });
  } else {
    next(); // Caso contrário, permite o acesso
  }
});

export default router;
