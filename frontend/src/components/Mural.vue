<script>
import ApiClient from '@/service/api';
import { reactive } from 'vue';

export default {
  data() {
    return {
      posts: [],
      load: false,
      weddingData: {
        uuid: null,
        id: null
      },
    };
  },
  methods: {
    back() {
      this.$router.push({
          name: 'Menu',
          query: {
            uuid: this.weddingData.uuid
          }
        });
    },
    async fetchPosts() {
      try {
          const apiClient = new ApiClient();

        const response = await apiClient.get(`/api/posts/${this.weddingData.id}`);
        this.posts = response.sort((a, b) => b.id - a.id); // Ordena por id decrescente

            // Transformar reações no formato correto
            this.posts.forEach((post) => {
              if (post.reactions) {
                const transformedReactions = {};
                Object.keys(post.reactions).forEach((key) => {
                  try {
                    // Parse a chave JSON para obter a reação real
                    const parsedKey = JSON.parse(key).reaction;
                    transformedReactions[parsedKey] = post.reactions[key];
                  } catch (error) {
                    console.error('Erro ao parsear chave de reação:', key, error);
                  }
                });
                post.reactions = reactive(transformedReactions);
              } else {
                // Inicializa as reações padrão caso não existam
                post.reactions = reactive({
                  '👍': 0,
                  '😍': 0,
                  '❤️': 0,
                  '😂': 0,
                  '🎉': 0
                });
              }
            });


        } catch (error) {
          console.error('Erro ao carregar os posts:', error);
        }
    },
    async addReaction(post, reaction) {
      if (!post.reactions) {
        post.reactions = reactive({});
      }

      if (!post.reactions[reaction]) {
        post.reactions[reaction] = 0;
      }

      post.reactions[reaction]++;

      try {
        const apiClient = new ApiClient();
        const response = await apiClient.post(`/api/posts/${post.id}/reactions`, {
          reaction,
        }, `${reaction} +`);
        post.reactions[reaction] = response.data.reactionCount;
      } catch (error) {
        console.error('Erro ao adicionar reação:', error);
      }
    },
    isImage(file) {
      return typeof file === 'string' && /\.(jpeg|jpg|gif|png)$/i.test(file);
    },
    isVideo(file) {
      return typeof file === 'string' && /\.(mp4|webm|ogg)$/i.test(file);
    },
    async fetchWeddingData() {
      const uuid = this.$route.query.uuid;
      if (!uuid) {
        console.error('UUID não encontrado na query string.');
        return;
      }

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
  async mounted() {
  await  this.fetchWeddingData();
  this.fetchPosts();

  },
};
</script>
<template>
  <div class="mural" v-if="load">

    <div v-if="posts.length > 0" class="post-container">
      <div v-for="post in posts" :key="post.id" class="post">
        <!-- Cabeçalho do post -->
        <div class="post-header">
          <b>{{ post.user.username }}</b>
        </div>

        <!-- Mídia do post -->
        <div class="post-media">
          <img v-if="isImage(post.mediaUrl)" :src="post.mediaUrl" alt="Imagem enviada">
          <video v-if="isVideo(post.mediaUrl)" controls :src="post.mediaUrl"></video>
        </div>

        <!-- Conteúdo do post -->
        <div class="post-content">
          <p>{{ post.content }}</p>
        </div>

        <!-- Reações -->
        <div class="post-reactions">
          <span class="reaction like" @click="addReaction(post, '👍')">👍 {{ post.reactions['👍'] || 0 }}</span>
          <span class="reaction love" @click="addReaction(post, '😍')">😍 {{ post.reactions['😍'] || 0 }}</span>
          <span class="reaction heart" @click="addReaction(post, '❤️')">❤️ {{ post.reactions['❤️'] || 0 }}</span>
          <span class="reaction laugh" @click="addReaction(post, '😂')">😂 {{ post.reactions['😂'] || 0 }}</span>
          <span class="reaction party" @click="addReaction(post, '🎉')">🎉 {{ post.reactions['🎉'] || 0 }}</span>
        </div>
      </div>
    </div>
    <div v-else class="no-posts">
      <p>Ainda não há mensagens no mural.</p>
    </div>
    <div class="field is-grouped is-grouped-left p-2">
        <div class="control">
          <button @click="back()" class="button btn-secondary">
          <span class="btn-icon">←</span>
          Voltar
        </button>
        </div>
    </div>
  </div>
</template>

<style scoped>
.mural {
  margin: 2rem auto;
  max-width: 600px;
  padding: 0 1rem;
  background-color: #fdf4e3;
}

.post-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.post {
  background: white;
  border: 1px solid #ffd1dc;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(255, 182, 193, 0.2);
  transition: transform 0.2s ease;
}

.post:hover {
  transform: translateY(-2px);
}

.post-header {
  padding: 1rem;
  border-bottom: 1px solid #ffd1dc;
  background: linear-gradient(to right, #ffb6c1, #ffd1dc, #ffe4e1);
  color: #d4516f;
  font-size: 1.1rem;
}

.post-media {
  width: 100%;
  background-color: #fff5f6;
  position: relative;
}

.post-media img,
.post-media video {
  width: 100%;
  max-height: 600px;
  object-fit: cover;
  display: block;
}

.post-content {
  padding: 1.2rem;
  line-height: 1.6;
  background: white;
  color: #5d4037;
}

.post-reactions {
  padding: 0.8rem 1rem;
  border-top: 1px solid #ffd1dc;
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
  background: #fff9fa;
}

.reaction {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
}

.reaction:hover {
  transform: scale(1.05);
}

.reaction.like {
  background: #fff0f3;
  color: #d4516f;
}

.reaction.love {
  background: #ffe8ec;
  color: #cc4466;
}

.reaction.heart {
  background: #ffeaef;
  color: #d94a75;
}

.reaction.laugh {
  background: #fff2f5;
  color: #c9436b;
}

.reaction.party {
  background: #fff5f7;
  color: #bf4265;
}

.no-posts {
  text-align: center;
  padding: 3rem;
  background: white;
  border-radius: 12px;
  color: #d4516f;
  box-shadow: 0 4px 15px rgba(255, 182, 193, 0.2);
}

/* Ajustes para mobile */
@media screen and (max-width: 768px) {
  .mural {
    margin: 0;
    padding: 0;
    max-width: 100%;
  }

  .post-container {
    gap: 1.5rem;
    padding: 1rem;
  }

  .post {
    margin: 0 0 1rem 0;
    border-radius: 8px;
  }

  .post-header {
    padding: 1rem;
  }

  .post-content {
    padding: 1rem;
  }

  .post-reactions {
    padding: 1rem;
    justify-content: center;
  }

  .reaction {
    padding: 0.5rem 0.8rem;
    font-size: 0.9rem;
  }
}

/* Ajustes específicos para telas muito pequenas */
@media screen and (max-width: 480px) {
  .post-container {
    padding: 0.8rem;
  }

  .reaction {
    padding: 0.4rem 0.6rem;
    font-size: 0.85rem;
  }
}
</style>