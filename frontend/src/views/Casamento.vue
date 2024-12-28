<script setup>
import { ref, reactive, onMounted } from 'vue';
import ApiClient from '@/service/api';

const apiClient = new ApiClient();

const weddingData = reactive({
  uuid: '',
  brideAndGroomName: '',
  imageUrl: '',
  leaveMessageUrl: '',
  viewMuralUrl: '',
  loginUrl: '',
});

const isLoginModalActive = ref(false);
const isLoading = ref(false);
const loginForm = ref({
  email: '',
  password: '',
  rememberMe: false,
});

const toggleLoginModal = () => {
  isLoginModalActive.value = !isLoginModalActive.value;
};

const handleLogin = async () => {
  isLoading.value = true;
  // Simular delay de login
  await new Promise((resolve) => setTimeout(resolve, 1000));
  isLoading.value = false;
  // Implementar lógica de login aqui
};

// Função para buscar os dados do casamento
const fetchWeddingData = async () => {
  try {
    const response = await apiClient.get('/api/wedding-data/uuid/371fbd9d-9d3c-4d6e-b358-129ee34b4729');

    Object.assign(weddingData, response);
    localStorage.setItem('uuid', weddingData.uuid)
    localStorage.setItem('wedding_data_id', weddingData.id)

  } catch (error) {
    console.error('Erro ao buscar dados do casamento:', error);
  }
};

// Chama a função ao montar o componente
onMounted(() => {
  fetchWeddingData();
});
</script>

<template>
  <router-view />
  <!-- Hero Section with Background Image -->
  <section class="hero is-medium">
    <div class="hero-background" :style="{ backgroundImage: `url(${weddingData.imageUrl})` }"></div>
    <div class="hero-head">
      <nav class="navbar is-transparent">
        <div class="container">
          <div class="navbar-menu is-active">
            <div class="navbar-end">
              <div class="navbar-item">
                <button class="button is-light is-small has-text-weight-medium login-button" @click="toggleLoginModal">
                  <span class="icon">
                    <i class="fas fa-user"></i>
                  </span>
                  <span>Área da Noiva</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </nav>
    </div>
    <div class="hero-body">
      <div class="container has-text-centered">
        <h1 class="title is-1 has-text-white has-text-weight-bold wedding-title">
          {{ weddingData.brideAndGroomName }}
        </h1>
      </div>
    </div>
  </section>

  <!-- Actions Section -->
  <section class="section">
    <div class="container">
      <div class="box action-box has-text-centered">
        <div class="buttons is-centered">
          <router-link
            :to="{ name: 'CadastroConvidado', params: { uuid: weddingData.uuid ? weddingData.uuid : 'new' } }"
            class="button is-primary is-rounded is-medium">
            <span class="icon">
              <i class="fas fa-pen"></i>
            </span>
            <span>Deixe sua mensagem</span>
          </router-link>


        </div>
      </div>
    </div>
  </section>

  <!-- Login Modal with Bulma Classes -->
  <div class="modal" :class="{ 'is-active': isLoginModalActive }">
    <div class="modal-background" @click="toggleLoginModal"></div>
    <div class="modal-card">
      <header class="modal-card-head has-background-primary-light">
        <p class="modal-card-title has-text-primary">
          <span class="icon-text">
            <span class="icon">
              <i class="fas fa-user-circle"></i>
            </span>
            <span>Área da Noiva</span>
          </span>
        </p>
        <button class="delete" aria-label="close" @click="toggleLoginModal"></button>
      </header>
      <section class="modal-card-body">
        <div class="field">
          <label class="label">Email</label>
          <div class="control has-icons-left">
            <input class="input" type="email" placeholder="seu@email.com" v-model="loginForm.email">
            <span class="icon is-small is-left">
              <i class="fas fa-envelope"></i>
            </span>
          </div>
        </div>

        <div class="field">
          <label class="label">Senha</label>
          <div class="control has-icons-left">
            <input class="input" type="password" placeholder="Sua senha" v-model="loginForm.password">
            <span class="icon is-small is-left">
              <i class="fas fa-lock"></i>
            </span>
          </div>
        </div>

        <div class="field">
          <div class="control">
            <label class="checkbox">
              <input type="checkbox" v-model="loginForm.rememberMe">
              Lembrar-me
            </label>
          </div>
        </div>

        <div class="has-text-right">
          <a class="is-size-7 has-text-primary">Esqueceu a senha?</a>
        </div>
      </section>
      <footer class="modal-card-foot is-justify-content-flex-end has-background-white-bis">
        <button class="button" @click="toggleLoginModal">
          Cancelar
        </button>
        <button class="button is-primary" :class="{ 'is-loading': isLoading }" @click="handleLogin">
          <span class="icon">
            <i class="fas fa-sign-in-alt"></i>
          </span>
          <span>Entrar</span>
        </button>
      </footer>
    </div>
  </div>
</template>

<style scoped>
.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  opacity: 0.8;
}

.hero {
  position: relative;
  overflow: hidden;
  height: 50vh;
}

.hero-body {
  z-index: 1;
  background-color: rgba(0, 0, 0, 0.3);
}

.navbar {
  background: transparent;
  padding: 1rem;
  position: relative;
  width: 100%;
}

.navbar-menu {
  background: transparent !important;
  box-shadow: none !important;
  padding: 0;
}

.navbar-end {
  margin-left: auto;
  display: flex;
  justify-content: flex-end;
}

.login-button {
  margin-right: 1rem;
}

.wedding-title {
  font-family: 'Playfair Display', 'Georgia', serif;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  font-size: 3rem !important;
}

.action-box {
  margin-top: -4rem;
  background-color: white;
  border-radius: 1rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.button {
  transition: all 0.3s ease;
  margin: 0.5rem;
}

.action-box .button {
  min-width: 200px;
}

.button.is-primary {
  background-color: #ff9a9e;
}

.button.is-primary:hover {
  background-color: #fecfef;
  transform: translateY(-3px);
}

.button.is-info {
  background-color: #81d4fa;
}

.button.is-info:hover {
  background-color: #b3e5fc;
  transform: translateY(-3px);
}

.modal-card {
  max-width: 400px;
  width: 90%;
}

.modal-card-title {
  font-size: 1.25rem;
}

.checkbox {
  cursor: pointer;
}

@media screen and (max-width: 768px) {
  .hero {
    height: 40vh;
  }

  .navbar-menu {
    display: block !important;
    padding: 0;
  }

  .navbar-item {
    justify-content: flex-end;
    padding-right: 1rem;
  }

  .login-button {
    margin-right: 0;
  }

  .wedding-title {
    font-size: 2rem !important;
  }

  .action-box {
    margin-top: -2rem;
    margin: -2rem 1rem 0;
  }

  .buttons {
    flex-direction: column;
  }

  .action-box .button {
    width: 100%;
  }
}
</style>
