<template>
  <div v-if="load">
    <!-- Header fixo -->
    <nav class="navbar is-fixed-top has-shadow" role="navigation" aria-label="main navigation">
      <div class="navbar-brand">
        <div class="navbar-item is-size-4 has-text-weight-bold is-flex is-justify-content-center is-flex-grow-1">
          💍 Mural dos Noivos 💍
        </div>
      </div>
    </nav>

    <!-- Container principal -->
    <div class="container mt-6 pt-6">
      <!-- Menu principal -->
      <div v-if="currentView === 'menu'" class="section">
        <div class="buttons is-flex is-flex-direction-column">

          <button class="button is-info is-fullwidth is-medium" @click="shareWeddingLink"
            v-if="this.weddingData.uuid && role == 'ROLE_MODERATOR'">
            <span class="icon">🔗</span>
            <span>Compartilhar Link</span>
          </button>

          <button class="button is-primary is-fullwidth is-medium" @click="navigateToCasamento"
            v-if="role == 'ROLE_MODERATOR'">
            <span class="icon"> 📝 </span>
            <span>Dados Casamento</span>
          </button>

          <button class="button is-primary is-fullwidth is-medium" @click="changeView('message')"
            v-if="this.weddingData.uuid">
            <span class="icon">💌</span>
            <span>Deixar Mensagem</span>
          </button>

          <button class="button is-primary is-fullwidth is-medium" @click="navigateToCamera"
            v-if="this.weddingData.uuid">
            <span class="icon">📸</span>
            <span>Foto ou Vídeo</span>
          </button>

          <button class="button is-primary is-fullwidth is-medium" @click="navigateToMural"
            v-if="this.weddingData.uuid">
            <span class="icon">🖼️</span>
            <span>Ver Mural</span>
          </button>
        </div>
      </div>
      <!-- Formulário de mensagem -->
      <div v-if="currentView === 'message'" class="section">
        <div class="box">
          <div class="field">
            <label class="label">Sua Mensagem aos Noivos</label>
            <div class="control">
              <textarea class="textarea" placeholder="Escreva uma mensagem especial..."
                v-model="newPost.content"></textarea>
            </div>
          </div>

          <div class="field is-grouped is-grouped-right">

            <p class="control">

              <button class="button btn-secondary" v-if="this.weddingData.uuid" @click="changeView('menu')">
                <span class="btn-icon">←</span>
                Voltar
              </button>
            </p>
            <p class="control">
              <button @click="sendPost" class="button btn-success">
                <span class="btn-icon">✓</span>
                Enviar
              </button>
            </p>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>
<script>
import ApiClient from '@/service/api';
import { useToast } from 'vue-toastification';

export default {
  data() {
    return {
      toast: useToast(),
      userName: 'Convidado123',
      newPost: {
        content: '',
        media: null,
        mediaName: '',
        username: ''
      },
      role: null,
      posts: [],
      currentView: 'menu',
      load: false,
      weddingData: {
        uuid: null,
        id: null
      }
    };
  },
  methods: {
    async shareWeddingLink() {
      const url = `${window.location.origin}/casamento?uuid=${this.weddingData.uuid}`;
      try {
        await navigator.clipboard.writeText(url);
        this.toast.success('Link copiado para a área de transferência!');
      } catch (error) {
        this.toast.error('Erro ao copiar o link.');
      }
    },
    navigateToCasamento() {

      this.$router.push({
        name: 'dados-casamento',
        query: {
          uuid: this.weddingData.uuid
        }
      });

    },
    navigateToMural() {

      this.$router.push({
        name: 'mural',
        query: {
          uuid: this.weddingData.uuid
        }
      });
    },
    navigateToCamera() {

      this.$router.push({
        name: 'CaptureMedia',
        query: {
          uuid: this.weddingData.uuid
        }
      });
    },
    changeView(view) {
      this.currentView = view;

    },

    async sendPost() {
      if (this.newPost.content || this.newPost.media) {
        // Constrói o objeto payload corretamente
        const payload = {
          content: this.newPost.content || '',
          mediaUrl: '', // 
          mediaFolder: '/uploads/wedding-photos',
          mediaType: this.newPost.media
            ? (this.newPost.media.type.startsWith('image/') ? 'image' : 'video')
            : null,
          user: { id: localStorage.getItem('userId') }, // ID do usuário
          weddingData: { id: this.weddingData.id }, // ID do evento
        };

        // Se houver mídia, adiciona ao FormData
        try {
          const apiClient = new ApiClient();
          const response = await apiClient.post('/api/posts', payload);

          // Adiciona o novo post à lista de posts
          this.posts.push(response.data);

          // Reseta o formulário
          this.newPost = { content: '', media: null, mediaName: '' };

          this.$router.push({
            name: 'mural',
            query: {
              uuid: this.weddingData.uuid
            }
          });

        } catch (error) {
          console.error('Erro ao enviar a postagem:', error);

        }

      } else {
        this.toast.error('Escreva uma mensagem para enviar ao Noivos!');

      }
    },
    async fetchWeddingData() {
      const uuid = this.$route.query.uuid;

      if (uuid == 'noivos' && uuid) {
        this.load = true;
        return
      }

      if (!uuid) {
        console.error('UUID não encontrado na query string.');
        return;
      }

      if (uuid != 'noivos' && uuid) {
        try {
          const apiClient = new ApiClient();

          const response = await apiClient.get(`/api/wedding-data/uuid/${uuid}`);

          Object.assign(this.weddingData, response);
          this.load = true;

        } catch (error) {
          console.error('Erro ao buscar dados do casamento:', error);
        }
      }

    },
  },
  async mounted() {
    await this.fetchWeddingData();
    this.role = localStorage.getItem('roles')
  },
};
</script>

<style scoped>
/* Estilos adicionais para complementar o Bulma */
.navbar-brand {
  min-height: 4rem;
}

.buttons {
  gap: 1rem;
}

.card {
  border-radius: 8px;
  box-shadow: 0 2px 3px rgba(10, 10, 10, 0.1);
}

.card-header {
  box-shadow: none;
  border-bottom: 1px solid #dbdbdb;
}

.image.is-square img {
  object-fit: cover;
}

/* Ajustes para mobile */
@media screen and (max-width: 768px) {
  .section {
    padding: 1.5rem 1rem;
  }

  .card {
    border-radius: 0;
    margin-left: -1rem;
    margin-right: -1rem;
  }
}
</style>