<template>
  <div class="app" v-if="load">
    <div class="camera-container" v-if="!isCaptured">
      <!-- Video preview -->
      <video ref="video" autoplay class="camera-preview"></video>

      <!-- Countdown overlay -->
      <div v-if="isRecording" class="countdown">{{ countdown }}</div>
    </div>

    <!-- Captured media preview -->
    <div v-if="isCaptured" class="captured-media">
      <img v-if="isPhoto" :src="photo" alt="Captured Photo" class="captured-content" />
      <video v-if="isVideo" controls :src="videoSrc" class="captured-content"></video>
    </div>

    <!-- Action buttons -->
    <div class="action-buttons">
      <div v-if="!isCaptured" class="controls">
        <button @click="back()" class="btn btn-secondary">
          <span class="btn-icon">←</span>
          Voltar
        </button>
        <button @click="startCapture('photo')" class="btn btn-primary">
          <span class="btn-icon">📸</span>
          Tirar Foto
        </button>
        <button @click="startCapture('video')" class="btn btn-primary">
          <span class="btn-icon">🎥</span>
          Gravar Vídeo
        </button>
        <button @click="toggleCamera()" class="btn btn-secondary">
          <span class="btn-icon">🔄</span>
          Alternar Câmera
        </button>
      </div>

      <div v-if="isCaptured" class="controls">
        <button @click="back()" class="btn btn-secondary">
          <span class="btn-icon">←</span>
          Voltar
        </button>
        <button @click="resetCapture" class="btn btn-primary">
          <span class="btn-icon">📸</span>
          Tentar Novamente
        </button>
        <button @click="sendCapture" class="btn btn-success">
          <span class="btn-icon">✓</span>
          Enviar
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ApiClient from '@/service/api';

export default {
  name: "CaptureMedia",
  data() {
    return {
      load: false,
      weddingData: {
        uuid: null,
        id: null
      },
      countdown: 5, // Contagem regressiva para vídeo
      isRecording: false, // Estado da gravação
      isCaptured: false, // Se a captura foi realizada
      isPhoto: false, // Se foi uma foto
      isVideo: false, // Se foi um vídeo
      photo: null, // URL da foto capturada
      videoSrc: null, // URL do vídeo gravado
      mediaStream: null, // Para armazenar o stream da câmera
      videoBlob: null, // Blob do vídeo gravado
      usingFrontCamera: true, // Define a câmera utilizada
    };
  },
  methods: {
    async startCapture(type) {
      if (type === "video") {
        this.captureVideo();
      } else if (type === "photo") {
        this.capturePhoto();
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
    },
    async capturePhoto() {
      const videoElement = this.$refs.video;
      const canvas = document.createElement("canvas");
      const context = canvas.getContext("2d");

      // Desenhar o quadro atual do vídeo no canvas
      canvas.width = videoElement.videoWidth;
      canvas.height = videoElement.videoHeight;
      context.drawImage(videoElement, 0, 0, canvas.width, canvas.height);

      // Converter o canvas em uma URL de imagem (data URL)
      this.photo = canvas.toDataURL("image/png");
      this.stopCamera();

      this.isCaptured = true;
      this.isPhoto = true;
    },

    captureVideo() {
      const videoElement = this.$refs.video;
      const mediaRecorder = new MediaRecorder(this.mediaStream); // Inclui áudio e vídeo
      let chunks = [];
      let timeLeft = this.countdown;

      mediaRecorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          chunks.push(event.data);
        }
      };

      mediaRecorder.onstop = () => {
        const blob = new Blob(chunks, { type: "video/webm" });
        this.videoSrc = URL.createObjectURL(blob);
        this.videoBlob = blob; // Armazena o blob diretamente para envio posterior
        this.stopCamera();

        this.isCaptured = true;
        this.isVideo = true;
      };

      mediaRecorder.start();

      this.isRecording = true;
      const countdownInterval = setInterval(() => {
        this.countdown = timeLeft;
        timeLeft -= 1;
        if (timeLeft < 0) {
          clearInterval(countdownInterval);
          this.countdown = "";
        }
      }, 1000);

      setTimeout(() => {
        mediaRecorder.stop();
        this.isRecording = false;
      }, 5000); // Captura por 5 segundos
    },

    stopCamera() {
      if (this.mediaStream) {
        const tracks = this.mediaStream.getTracks();
        tracks.forEach((track) => track.stop());
      }
      this.$refs.video.srcObject = null; // Desconectar a câmera
    },

    async getMediaStream(facingMode = "user") {
      try {
        // Solicita acesso à câmera e ao microfone
        this.mediaStream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode }, // "user" para câmera frontal, "environment" para traseira
          audio: true, // Inclui áudio
        });

        // Aguarda o DOM estar atualizado antes de acessar o elemento
        this.$nextTick(() => {
          const videoElement = this.$refs.video;
          if (videoElement) {
            videoElement.srcObject = this.mediaStream; // Atribui o stream à tag <video>
          } else {
            console.error("Elemento <video> não encontrado no DOM.");
          }
        });
      } catch (error) {
        console.error("Erro ao acessar os dispositivos de mídia:", error);

        // Mostra uma mensagem de erro ao usuário, se necessário
        alert("Não foi possível acessar a câmera e o microfone. Verifique as permissões.");
      }
    },

    async toggleCamera() {
      const currentFacingMode = this.mediaStream.getVideoTracks()[0].getSettings().facingMode;
      const newFacingMode = currentFacingMode === "user" ? "environment" : "user";

      // Para a câmera atual
      this.mediaStream.getTracks().forEach((track) => track.stop());

      // Obtém a nova câmera
      await this.getMediaStream(newFacingMode);
    },

    resetCapture() {
      this.isCaptured = false;
      this.isPhoto = false;
      this.isVideo = false;
      this.photo = null;
      this.videoSrc = null;
      this.videoBlob = null; // Limpa o blob armazenado
      const facingMode = this.usingFrontCamera ? "user" : "environment";
      this.getMediaStream(facingMode); // Reiniciar a câmera
    },

    async sendCapture() {
      if (this.isPhoto && this.photo) {
        // Converte a foto capturada (data URL) para Blob
        const blob = this.dataURLToBlob(this.photo);
        const mimeType = blob.type || 'image/png'; // Detecta o tipo MIME
        const mediaType = mimeType.split('/')[1]; // Extrai a extensão

        const formData = new FormData();
        formData.append("media", blob);
        formData.append("content", ""); // Adicione conteúdo, se necessário
        formData.append("userId", localStorage.getItem('userId')); // Adicione conteúdo, se necessário
        formData.append("weddingDataId", this.weddingData.id); // Adicione conteúdo, se necessário
        formData.append("mediaType", mediaType); // 'webm', 'mp4', etc.
        this.sendPost(formData);
      } else if (this.isVideo && this.videoBlob) {
        // Envia diretamente o blob armazenado
        const mimeType = this.videoBlob.type || 'video/webm'; // Detecta o tipo MIME
        const mediaType = mimeType.split('/')[1]; // Extrai a extensão

        const formData = new FormData();
        formData.append("media", this.videoBlob);
        formData.append("content", ""); // Adicione conteúdo, se necessário
        formData.append("userId", localStorage.getItem('userId')); // Adicione conteúdo, se necessário
        formData.append("weddingDataId", this.weddingData.id); // Adicione conteúdo, se necessário
        formData.append("mediaType", mediaType); // 'webm', 'mp4', etc.

        this.sendPost(formData);
      }
    },
    handleVisibilityChange() {
      if (document.visibilityState === 'hidden') {
        this.stopCamera();
      }
    },
    async sendPost(formData) {
      try {
        const apiClient = new ApiClient();
        const response = await apiClient.postFormData('/api/posts/midia', formData);

        this.$router.push({
          name: 'mural',
          query: {
            uuid: this.weddingData.uuid
          }
        });
      } catch (error) {
        console.error('Erro ao carregar os posts:', error);
      }
    },

    dataURLToBlob(dataURL) {
      const arr = dataURL.split(",");
      const mime = arr[0].match(/:(.*?);/)[1];
      const bstr = atob(arr[1]);
      let n = bstr.length;
      const u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], { type: mime });
    },
  },
  async mounted() {
    await this.fetchWeddingData();
    this.getMediaStream();

    document.addEventListener('visibilitychange', this.handleVisibilityChange);
  },
  beforeDestroy() {
    this.stopCamera();
    document.removeEventListener('visibilitychange', this.handleVisibilityChange);
  }

};
</script>


<style scoped>
.app {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 20px;
  position: relative;
}

.camera-container {
  width: 100%;
  max-width: 800px;
  height: 70vh;
  position: relative;
  margin: 0 auto;
  overflow: hidden;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  background-color: #000;
}

.camera-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.countdown {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 72px;
  color: #ffffff;
  font-weight: bold;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
  z-index: 10;
}

.captured-media {
  width: 100%;
  max-width: 800px;
  margin: 20px auto;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.captured-content {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 16px;
}

.action-buttons {
  width: 100%;
  max-width: 800px;
  margin-top: 20px;
  padding: 16px;
}

.controls {
  display: flex;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 160px;
}

.btn-icon {
  font-size: 20px;
}

.btn-primary {
  background-color: #c991a4;
  color: white;
}

.btn-primary:hover {
  background-color: #b67d91;
  transform: translateY(-2px);
}

.btn-secondary {
  background-color: #e24949;
  color: white;
}

.btn-secondary:hover {
  background-color: #c93e3e;
  transform: translateY(-2px);
}

.btn-success {
  background-color: #4CAF50;
  color: white;
}

.btn-success:hover {
  background-color: #3d8b40;
  transform: translateY(-2px);
}

@media (max-width: 768px) {
  .camera-container {
    height: 60vh;
  }

  .btn {
    padding: 10px 20px;
    min-width: 140px;
    font-size: 14px;
  }

  .countdown {
    font-size: 48px;
  }

  .controls {
    gap: 12px;
  }
}
</style>