<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRoute } from 'vue-router'; // Importa o useRoute para acessar os parâmetros da rota
import ApiClient from '@/service/api';

const apiClient = new ApiClient();
const route = useRoute(); // Hook para acessar a query string

const token = localStorage.getItem('accessToken')

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

const goToLogin = () => {
  router.push('/auth/signin')
}
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
  const uuid = route.query.uuid; // Obtém o uuid da query string
  if (!uuid) {
    console.error('UUID não encontrado na query string.');
    return;
  }

  try {
    const response = await apiClient.get(`/api/wedding-data/uuid/${uuid}`);

    Object.assign(weddingData, response);
    localStorage.setItem('uuid', weddingData.uuid);
    localStorage.setItem('wedding_data_id', weddingData.id);
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
  <section class="hero is-fullheight-with-navbar">
    <div class="hero-background"
      :style="{ backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url(${weddingData.imageUrl})` }">
    </div>

    <div class="hero-head">
      <nav class="navbar is-transparent py-4">
        <div class="container">
          <div class="navbar-menu">
            <div class="navbar-end">
              <div class="navbar-item">
                <router-link to="/auth/signin" class="button is-primary is-rounded" v-if="!token">
                  <span class="icon">
                  </span>
                  <i class="fas fa-user"></i>
                  <span>Login</span>
                </router-link>

              </div>
            </div>
          </div>
        </div>
      </nav>
    </div>

    <div class="hero-body">
      <div class="container">
        <div class="has-text-centered">
          <h1 class="title is-1 has-text-white has-text-weight-bold wedding-title">
            {{ weddingData.brideAndGroomName }}
          </h1>
        </div>
      </div>
    </div>
  </section>

  <section class="section action-section">
    <div class="container">
      <div class="box action-box has-text-centered">
        <div class="buttons is-centered">
          <router-link :to="{ name: 'Cadastro', query: { uuid: weddingData.uuid } }"
            class="button is-primary is-rounded is-medium action-button">
            <span class="icon">
              <i class="fas fa-pen"></i>
            </span>
            <span>Deixe sua mensagem</span>
          </router-link>
        </div>
      </div>
    </div>
  </section>

</template>

<style scoped>
.hero {
  position: relative;
  min-height: 70vh;
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: 0;
}

.hero-head,
.hero-body {
  position: relative;
  z-index: 1;
}

.navbar {
  background: transparent;
}

.navbar-menu {
  background: transparent !important;
}

.wedding-title {
  font-family: 'Playfair Display', serif;
  font-size: 4.5rem !important;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  line-height: 1.2;
  margin-bottom: 2rem;
}

.action-section {
  margin-top: -7rem;
  padding-bottom: 4rem;
}

.action-box {
  max-width: 500px;
  margin: 0 auto;
  border-radius: 1rem;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  border: none;
  padding: 2.5rem;
}

.action-button {
  min-width: 250px;
  height: 3.5rem;
  font-size: 1.1rem;
  transition: all 0.3s ease;
}

.modal-card {
  max-width: 400px;
  border-radius: 1rem;
  overflow: hidden;
}

.modal-card-head {
  border-bottom: 1px solid #f5f5f5;
}

.modal-card-foot {
  border-top: 1px solid #f5f5f5;
  padding: 1.5rem;
}

.input.is-rounded {
  height: 2.8rem;
}

@media screen and (max-width: 768px) {
  .hero {
    min-height: 50vh;
  }

  .wedding-title {
    font-size: 2.5rem !important;
  }

  .action-section {
    margin-top: -4rem;
  }

  .action-box {
    margin: 0 1rem;
    padding: 1.5rem;
  }

  .action-button {
    width: 100%;
  }

  .modal-card {
    margin: 1rem;
  }
}
</style>