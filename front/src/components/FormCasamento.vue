<template>
  <div class="container" v-if="load">
    <div class="section">
      <div class="box">
        <h1 class="title has-text-centered">Cadastro de Casamento</h1>

        <!-- Nome dos Noivos -->
        <div class="field" :class="{ 'is-danger': errors.brideAndGroomName }">
          <label class="label">Nome dos Noivos</label>
          <div class="control">
            <input class="input" type="text" placeholder="Digite como irá aparecer Maria & João"
              v-model="formData.brideAndGroomName" />
          </div>
          <p class="help is-danger" v-if="errors.brideAndGroomName">{{ errors.brideAndGroomName }}</p>
        </div>

        <!-- Data do Casamento -->
        <div class="field" :class="{ 'is-danger': errors.weddingDate }">
          <label class="label">Data do Casamento</label>
          <div class="control">
            <input class="input" type="date" v-model="formData.weddingDate" />
          </div>
          <p class="help is-danger" v-if="errors.weddingDate">{{ errors.weddingDate }}</p>
        </div>

        <!-- Cor do Tema -->
        <div class="field" :class="{ 'is-danger': errors.color }">
          <label class="label">Cor do Tema</label>
          <div class="control">
            <input class="input" type="color" v-model="formData.color" />
          </div>
          <p class="help is-danger" v-if="errors.color">{{ errors.color }}</p>
        </div>

        <!-- Upload da Imagem -->
        <div class="field" :class="{ 'is-danger': errors.image }">
          <label class="label">Imagem de Perfil</label>
          <div class="control">
            <input class="input" type="file" accept="image/*" @change="handleImageUpload" />
          </div>
          <p class="help is-danger" v-if="errors.image">{{ errors.image }}</p>
        </div>

        <!-- Exibição da Imagem -->
        <div v-if="imagePreview" class="field">
          <div class="control">
            <img :src="imagePreview" alt="Pré-visualização da Imagem" style="max-width: 100%; max-height: 200px;" />
          </div>
        </div>

        <!-- Botão para Cortar Imagem -->
        <div v-if="imagePreview" class="field">
          <button class="button is-link" @click="openCropModal">Cortar Imagem</button>
        </div>

        <!-- Modal de Crop -->
        <div v-if="showModal" class="modal is-active">
          <div class="modal-background" @click="closeModal"></div>
          <div class="modal-content">
            <div class="box">
              <div class="image-container">
                <img ref="image" :src="imagePreview" alt="Cortar Imagem" />
              </div>
              <div class="field is-grouped is-grouped-centered">
                <button class="button is-primary" @click="applyCrop">Aplicar Corte</button>
                <button class="button" @click="closeModal">Cancelar</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Botão de Envio -->
        <div class="field">
          <div class="field is-grouped is-grouped-right">
            <div class="control">
              <button @click="back()" class="button is-alert"> Voltar</button>

            </div>
            <button class="button is-primary" @click="submitForm">Enviar</button>


          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import Cropper from 'cropperjs';
import 'cropperjs/dist/cropper.css';
import ApiClient from '@/service/api';
import { useToast } from 'vue-toastification';

export default {
  data() {
    return {
      toast: useToast(),
      role: null,
      load: false,
      weddingData: {

      },
      formData: {
        brideAndGroomName: '',
        weddingDate: '',
        color: '#ffffff',
        userId: localStorage.getItem('userId'),
        mediaType: '',
        imageFile: null,
      },
      imagePreview: null, // Pré-visualização da imagem
      croppedBlob: null, // Imagem cortada em formato Blob
      cropper: null, // Instância do Cropper.js
      showModal: false, // Controle do modal
      errors: {}, // Erros de validação
    };
  },
  methods: {
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

    back() {
      this.$router.push({
        name: 'Menu',
        query: {
          uuid: this.weddingData.uuid
        }
      });
    },
    handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.formData.imageFile = file;
        this.formData.mediaType = file.type;

        const reader = new FileReader();
        reader.onload = (e) => {
          this.imagePreview = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    openCropModal() {
      this.showModal = true;
      this.$nextTick(() => {
        const image = this.$refs.image;
        if (this.cropper) {
          this.cropper.destroy();
        }
        this.cropper = new Cropper(image, {
          aspectRatio: 1,
          viewMode: 2,
          autoCropArea: 0.8,
        });
      });
    },
    applyCrop() {
      if (this.cropper) {
        this.cropper.getCroppedCanvas().toBlob((blob) => {
          this.croppedBlob = blob;
          this.imagePreview = URL.createObjectURL(blob);
          this.closeModal();
        });
      }
    },
    closeModal() {
      this.showModal = false;
      if (this.cropper) {
        this.cropper.destroy();
        this.cropper = null;
      }
    },
    validateForm() {
      this.errors = {};

      if (!this.formData.brideAndGroomName) {
        this.errors.brideAndGroomName = 'O nome dos noivos é obrigatório.';
      }
      if (!this.formData.weddingDate) {
        this.errors.weddingDate = 'A data do casamento é obrigatória.';
      }
      if (!this.formData.color) {
        this.errors.color = 'A cor do tema é obrigatória.';
      }
      if (!this.imagePreview) {
        this.errors.image = 'A imagem de perfil é obrigatória.';
      }

      // Validação adicional para o tamanho do blob
      const imageToSend = this.croppedBlob || this.formData.imageFile;
      if (imageToSend) {
        const maxSize = 20 * 1024 * 1024; // 20 MB para imagens
        if (imageToSend.size > maxSize && this.formData.mediaType !== 'video') {
          this.errors.image = 'A imagem excede o tamanho máximo permitido de 20 MB.';
          return
        }

        if (this.formData.mediaType === 'video') {
          const maxVideoSize = 50 * 1024 * 1024; // 50 MB para vídeos
          if (imageToSend.size > maxVideoSize) {

            this.errors.image = 'O vídeo excede o tamanho máximo permitido de 50 MB.';
            this.toast.error(this.errors.image);

          }
        }
      }

      return Object.keys(this.errors).length === 0;
    },
    async submitForm() {
      if (!this.validateForm()) return;

      const formData = new FormData();
      formData.append('brideAndGroomName', this.formData.brideAndGroomName);
      formData.append('weddingDate', this.formData.weddingDate);
      formData.append('color', this.formData.color);
      formData.append('userId', this.formData.userId);
      formData.append('mediaType', this.formData.mediaType);

      const imageToSend = this.croppedBlob || this.formData.imageFile;
      if (imageToSend) {
        formData.append('media', imageToSend, 'image.jpg');
      }

      try {
        const apiClient = new ApiClient();

        const response = await apiClient.postFormData('/api/casamento', formData);

        this.$router.push({
          name: 'Menu',
          query: {
            uuid: response.uuid
          }
        });
      } catch (error) {
        console.error('Erro ao enviar o formulário:', error);
      }
    }
  },
  async mounted() {
    await this.fetchWeddingData();
    this.role = localStorage.getItem('roles')
  },
  destroyed() {
    if (this.cropper) {
      this.cropper.destroy();
    }
  },
};
</script>
