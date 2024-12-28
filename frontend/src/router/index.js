import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/gravarVideo',
    name: 'CaptureMedia',
    component: () => import(/* webpackChunkName: "index2" */ '@/views/CaptureMedia.vue'),
    meta: { requiresAuth: true },  // Protege a rota
  },
  {
    path: '/auth/noiva/signup',
    name: 'CadastroNoiva',
    component: () => import(/* webpackChunkName: "index2" */ '@/components/CadastroNoiva.vue'),
    meta: { requiresNoAuth: true } // Adiciona meta para bloquear caso esteja logado
  },
  {
    path: '/auth/signin',
    name: 'Login',
    component: () => import(/* webpackChunkName: "index2" */ '@/components/Login.vue'),
    meta: { requiresNoAuth: true } // Adiciona meta para bloquear caso esteja logado
  },
  {
    path: '/auth/convidado/signup',
    name: 'CadastroConvidado',
    component: () => import(/* webpackChunkName: "index2" */ '@/components/CadastroConvidado.vue'),
    meta: { requiresNoAuth: true } // Adiciona meta para bloquear caso esteja logado
  },
  {
    path: '/mural',
    name: 'Mural',
    component: () => import(/* webpackChunkName: "index2" */ '@/views/Mural.vue'),
    meta: { requiresAuth: true },  // Protege a rota
  },
  {
    path: '/casamento',
    name: 'casamento',
    component: () => import(/* webpackChunkName: "index2" */ '@/views/Casamento.vue'),
    meta: { requiresNoAuth: true } // Adiciona meta para bloquear caso esteja logado

  },
];

const router = createRouter({
  history: createWebHistory(),
  routes, // Definindo as rotas
});

// Guarda de navegação global
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken'); // Obtém o token do localStorage

  // Verifica se a rota requer autenticação e se o usuário não está logado
  if (to.meta.requiresAuth && !token) {
    next('/auth/signin'); // Redireciona para a página de login se não houver token
  }
  // Verifica se a rota exige que o usuário não esteja logado (para impedir o acesso a cadastros)
  else if (to.meta.requiresNoAuth && token) {
    next('/mural'); // Redireciona para a página inicial ou outra página pública se já estiver logado
  } else {
    next(); // Caso contrário, permite o acesso
  }
});

export default router;
