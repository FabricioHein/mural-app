<template>
  <div class="app">
    <!-- Vídeo em tela cheia com altura de 80% -->
    <video ref="video" autoplay></video>

    <!-- Contador de tempo (apenas para vídeo) -->
    <div v-if="isRecording" class="countdown">{{ countdown }}</div>

    <!-- Exibição de Foto ou Vídeo Capturado -->
    <div v-if="isCaptured" class="captured-media">
      <img v-if="isPhoto" :src="photo" alt="Captured Photo" />
      <video v-if="isVideo" controls :src="videoSrc" />
    </div>

    <!-- Container para botões de ações -->
    <div class="action-buttons">
      <div v-if="!isCaptured" class="controls">
        <button @click="startCapture('photo')" class="capture-button">📸 Tirar Foto</button>
        <button @click="startCapture('video')" class="capture-button">🎥 Gravar Vídeo</button>
      </div>

      <div v-if="isCaptured" class="controls">
        <button @click="resetCapture" class="capture-button">Tentar Novamente</button>
        <button @click="sendCapture" class="capture-button">Enviar</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CaptureMedia",
  data() {
    return {
      countdown: 5, // Contagem regressiva para vídeo
      isRecording: false, // Estado da gravação
      isCaptured: false, // Se a captura foi realizada
      isPhoto: false, // Se foi uma foto
      isVideo: false, // Se foi um vídeo
      photo: null, // URL da foto capturada
      videoSrc: null, // URL do vídeo gravado
      mediaStream: null, // Para armazenar o stream da câmera
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
      const mediaRecorder = new MediaRecorder(videoElement.srcObject);
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

    async getMediaStream() {
      try {
        this.mediaStream = await navigator.mediaDevices.getUserMedia({
          video: true,
        });
        this.$refs.video.srcObject = this.mediaStream;
      } catch (error) {
        console.error("Error accessing media devices.", error);
      }
    },

    resetCapture() {
      this.isCaptured = false;
      this.isPhoto = false;
      this.isVideo = false;
      this.photo = null;
      this.videoSrc = null;
      this.getMediaStream(); // Reiniciar a câmera
    },

    async sendCapture() {
      if (this.isPhoto) {
        // Converte a foto capturada (data URL) para Blob
        const blob = this.dataURLToBlob(this.photo);
        const formData = new FormData();
        formData.append("media", blob, "photo.png");
        formData.append("content", ""); // Adicione conteúdo, se necessário

        this.sendPost(formData);
      } else if (this.isVideo) {
        // Obtém o Blob do vídeo a partir da URL criada
        const videoBlob = await this.getVideoBlob();
        if (videoBlob) {
          const formData = new FormData();
          formData.append("media", videoBlob, "video.webm");
          formData.append("content", ""); // Adicione conteúdo, se necessário

          this.sendPost(formData);
        }
      }
    },

    async sendPost(formData) {
      try {
        const response = await fetch("/api/posts", {
          method: "POST",
          body: formData,
        });

        if (response.ok) {
          const data = await response.json();
          console.log("Post enviado com sucesso:", data);

          // Reseta o estado da captura
          this.resetCapture();
        } else {
          throw new Error("Erro no envio do post");
        }
      } catch (error) {
        console.error("Erro ao enviar a captura:", error);
        alert("Erro ao enviar a captura!");
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

    getVideoBlob() {
      return fetch(this.videoSrc)
        .then((res) => res.blob())
        .catch((err) => {
          console.error("Erro ao converter o vídeo:", err);
          return null;
        });
    },
  },

  mounted() {
    this.getMediaStream();
  },
};
</script>
<style scoped>
.app {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f9f9f9;  /* Cor suave de fundo para casamento */
  color: #333;
  position: relative;
  overflow: hidden;
}

video {
  width: 100%;
  height: 80%; /* Alterado para altura de 80% */
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 10px; /* Bordas arredondadas */
}

.countdown {
  font-size: 60px;
  color: #ff4081; /* Cor suave e chamativa para o contador */
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  font-weight: bold;
  text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5);
}

.controls {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding-bottom: 10px;
}

.capture-button {
  padding: 12px 30px;
  font-size: 20px;
  background-color: #e91e63; /* Cor delicada e elegante para casamento */
  color: white;
  border: none;
  border-radius: 50px; /* Botões arredondados */
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s ease;
}

.capture-button:hover {
  background-color: #c2185b; /* Cor mais escura ao passar o mouse */
  transform: translateY(-5px); /* Efeito de "flutuar" */
}

.capture-button:active {
  background-color: #880e4f; /* Cor ainda mais escura quando pressionado */
  transform: translateY(0);
}

.captured-media {
  margin-top: 20px;
  text-align: center;
}

img {
  max-width: 100%;
  height: auto;
  margin-top: 20px;
  border-radius: 10px; /* Bordas arredondadas para a imagem */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* Sombra suave para destacar */
}

video {
  width: 100%;
  margin-top: 20px;
  margin-right: 5px;
  margin-bottom: 5px;
  border-radius: 10px; /* Bordas arredondadas para o vídeo */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* Sombra suave para destacar */
}

.action-buttons {
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 20px;
  position: absolute;
  bottom: 0;
}

@media (max-width: 768px) {
  .controls {
    width: 90%;
  }

  .capture-button {
    font-size: 18px;
    padding: 10px 25px;
  }

  .countdown {
    font-size: 48px;
  }
}
</style>
