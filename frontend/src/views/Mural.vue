<script>
import ApiClient from '@/service/api';
import { reactive } from 'vue';

export default {
  data() {
    return {
      userName: 'Convidado123',
      newPost: {
        content: '',
        media: null,
        mediaName: '',
        username: ''
      },
      posts: [],
      currentView: 'menu'
    };
  },
  methods: {
    navigateToCamera() {
      this.$router.push('/gravarVideo');
    },
    changeView(view) {
      this.currentView = view;

      if (this.currentView == 'mural') {
        try {
          const apiClient = new ApiClient();


          apiClient.get(`/api/posts/${localStorage.getItem('wedding_data_id')}`).then((response) => {
            this.posts = response;

            // Garantir que todos os posts tenham a estrutura de reações inicializada
            this.posts.forEach(post => {
              if (!post.reactions) {
                post.reactions = reactive({});
              }
            });
          });
        } catch (error) {
          console.error('Erro ao carregar os posts:', error);
        }
      }
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file && this.validateFile(file)) {
        this.newPost.media = file;
        this.newPost.mediaName = file.name;
      }
    },
    validateFile(file) {
      const maxImageSize = 20 * 1024 * 1024; // 20 MB
      const maxVideoSize = 500 * 1024 * 1024; // 500 MB

      if (file.type.startsWith('image/') && file.size > maxImageSize) {
        alert('O tamanho da imagem não pode exceder 20 MB.');
        return false;
      }

      if (file.type.startsWith('video/') && file.size > maxVideoSize) {
        alert('O tamanho do vídeo não pode exceder 500 MB.');
        return false;
      }

      return true;
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
          weddingData: { id: localStorage.getItem('wedding_data_id') }, // ID do evento
        };

        // Se houver mídia, adiciona ao FormData
        try {
            const apiClient = new ApiClient();
            const response = await apiClient.post('/api/posts', payload);

            // Adiciona o novo post à lista de posts
            this.posts.push(response.data);

            // Reseta o formulário
            this.newPost = { content: '', media: null, mediaName: '' };
          } catch (error) {
            console.error('Erro ao enviar a postagem:', error);
            alert('Erro ao enviar a mensagem!');
          }

      } else {
        alert('Por favor, escreva uma mensagem ou envie uma mídia!');
      }
    },
    async addReaction(post, reaction) {
      // Se a propriedade reactions não existir, cria um objeto reativo para ela
      if (!post.reactions) {
        post.reactions = reactive({});
      }

      // Se a reação ainda não foi registrada, inicializa com 0
      if (!post.reactions[reaction]) {
        post.reactions[reaction] = 0;  // Cria a reação com valor 0
      }

      // Incrementa a reação localmente para feedback rápido
      post.reactions[reaction]++;

      try {
        // Envia a reação para o backend
        const apiClient = new ApiClient();
        const response = await apiClient.post(`/api/posts/${post.id}/reactions`, {
          reaction
        });

        // Atualiza a contagem de reações com a resposta do backend
        post.reactions[reaction] = response.data.reactionCount;
      } catch (error) {
        console.error('Erro ao adicionar reação:', error);
      }
    },
    isImage(file) {
      const imageExtensions = /\.(jpeg|jpg|gif|png)$/i;
      return typeof file === 'string' && imageExtensions.test(file);
    },
    isVideo(file) {
      const videoExtensions = /\.(mp4|webm|ogg)$/i;
      return typeof file === 'string' && videoExtensions.test(file);
    }
  },
  mounted() {

  }
};
</script>

<template>
  <div class="section">
    <div class="container">
      <div class="container">
        <!-- Banner -->
        <div class="banner">
          <h1 class="title">💍 Mural dos Noivos 💍</h1>
          <p class="subtitle">Escolha uma opção abaixo para começar!</p>
        </div>

        <!-- Botões de Navegação -->
        <div v-if="currentView === 'menu'" class="button-container">
          <button class="button is-primary" @click="changeView('message')">
            <span class="icon">💌</span>
            <span>Inserir uma Mensagem</span>
          </button>
          <!-- Botão para navegar para a página de captura -->
          <button class="button is-info" @click="navigateToCamera">
            <span class="icon">📸</span>
            <span>Gravar ou Tirar uma Imagem</span>
          </button>
          <button class="button is-success" @click="changeView('mural')">
            <span class="icon">🖼️</span>
            <span>Ver o Mural</span>
          </button>
        </div>

        <!-- Inserir Mensagem -->
        <div v-if="currentView === 'message'">
          <div class="field">
            <label class="label">Deixe sua mensagem</label>
            <textarea class="textarea" placeholder="Escreva uma mensagem especial aos noivos!"
              v-model="newPost.content"></textarea>
          </div>
          <button class="button is-primary" @click="sendPost">Enviar Mensagem</button>
          <button class="button is-light" @click="changeView('menu')">Voltar</button>
        </div>

        <!-- Captura de Imagem ou Vídeo -->
        <div v-if="currentView === 'capture'">
          <div class="field">
            <label class="label">Envie uma imagem ou vídeo</label>
            <input type="file" @change="handleFileUpload" accept="image/*,video/*">
          </div>
          <button class="button is-primary" @click="sendPost">Enviar</button>
          <button class="button is-light" @click="changeView('menu')">Voltar</button>
        </div>

        <!-- Mural -->
        <div v-if="currentView === 'mural'">
          <div v-if="posts.length > 0" class="post-container">
            <div v-for="post in posts" :key="post.id" class="post">
              <div class="post-content">
                <b>{{ post.username }}</b>
              </div>
              <div class="post-media">
                <img v-if="isImage(post.mediaUrl)" :src="post.mediaUrl" alt="Imagem enviada">
                <video v-if="isVideo(post.mediaUrl)" controls :src="post.mediaUrl"></video>
              </div>
              <div class="post-content">
                <p>{{ post.content }}</p>
              </div>
              <div class="post-reactions">
                <span class="reaction" @click="addReaction(post, '👍')">👍 {{ post.reactions['👍'] || 0 }}</span>
                <span class="reaction" @click="addReaction(post, '😍')">😍 {{ post.reactions['😍'] || 0 }}</span>
                <span class="reaction" @click="addReaction(post, '❤️')">❤️ {{ post.reactions['❤️'] || 0 }}</span>
                <span class="reaction" @click="addReaction(post, '😂')">😂 {{ post.reactions['😂'] || 0 }}</span>
                <span class="reaction" @click="addReaction(post, '🎉')">🎉 {{ post.reactions['🎉'] || 0 }}</span>
              </div>
            </div>
          </div>
          <div v-else>
            <p>Ainda não há mensagens no mural.</p>
          </div>
          <button class="button is-light" @click="changeView('menu')">Voltar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
body {
  background-color: #fdf4e3;
  font-family: 'Arial', sans-serif;
}

.banner {
  background: linear-gradient(135deg, #ff9a9e, #fecfef);
  color: white;
  padding: 2rem 0;
  text-align: center;
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

.button-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.button-container .button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.post-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1rem;
}

.post {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.post-content {
  padding: 1rem;
}

.post-media img,
.post-media video {
  width: 100%;
  display: block;
}

.reaction {
  margin: 0 5px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: transform 0.2s;
}

.reaction:hover {
  transform: scale(1.2);
}
</style>
