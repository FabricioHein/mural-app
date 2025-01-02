<template>
  <div class="container" v-if="load">
    <h2 class="title has-text-centered">Login</h2>
    <form @submit.prevent="handleSubmit" class="box">
      <div class="field">
        <label class="label" for="username">Username</label>
        <div class="control">
          <input class="input" type="text" id="username" v-model="formData.username" required
            placeholder="Digite o seu username" />
        </div>
      </div>

      <div class="field">
        <label class="label" for="password">Senha</label>
        <div class="control">
          <input class="input" :type="showPassword ? 'text' : 'password'" id="password" v-model="formData.password"
            required placeholder="Digite a sua senha" />
        </div>
      </div>

      <div class="field is-grouped is-grouped-centered">
        <div class="control">
          <button type="submit" class="button is-primary">
            Login
          </button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import ApiClient from '../service/api';

export default {
  data() {
    return {
      formData: {
        username: '',
        password: '',
      },
      passwordError: false,
      showPassword: false,
      load: false,
      weddingData: {
        uuid: null,
        id: null
      }
    };
  },
  async mounted() {
    await this.fetchWeddingData();
  },
  methods: {
    async fetchWeddingData() {
      const uuid = this.$route.query.uuid;
      try {
          const apiClient = new ApiClient();

          const response = await apiClient.get(`/api/wedding-data/uuid/${uuid}`);

          Object.assign(this.weddingData, response);
          this.load = true;

        } catch (error) {
          console.error('Erro ao buscar dados do casamento:', error);
        }

    },
    async handleSubmit() {


      this.passwordError = false; // Limpa o erro, caso a senha seja válida

      const apiClient = new ApiClient();
      try {
        const response = await apiClient.login(this.formData);

        // Armazenar o accessToken no localStorage
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('username', response.username);
        localStorage.setItem('userId', response.id);
        localStorage.setItem('roles', response.roles[0]);

        // Redirecionar para a página inicial ou página protegida
        this.$router.push({
          name: 'Menu',
          query: {
            uuid: this.weddingData.uuid !=  'novios' ? this.weddingData.uuid  : 'noivos' 
          }
        }); // ou outra rota que você queira
      } catch (error) {
        console.error('Erro ao realizar login:', error);

        // Se ocorrer erro, limpar os dados do localStorage
        localStorage.removeItem('accessToken');
        localStorage.removeItem('username');
        localStorage.removeItem('userId');
        localStorage.removeItem('roles');

        // Exibir mensagem de erro (opcional)
        this.passwordError = true;
      }
    },

    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
  },
};
</script>

<style scoped>
.container {
  margin-top: 50px;
  padding: 0 20px;
  max-width: 600px;
  margin: 50px auto;
}

.box {
  background-color: var(--branco-quente);
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 30px;
  margin: 20px 0;
}

.title {
  color: var(--rosa-principal);
  font-family: 'Georgia', serif;
  font-weight: bold;
}

.input {
  border-radius: 8px;
  border: 1px solid var(--rosa-escuro);
}

.button.is-primary {
  background-color: var(--rosa-principal);
  color: white;
  border-radius: 8px;
  border: none;
}

.button.is-primary:hover {
  background-color: var(--rosa-escuro);
  color: white;
}

.help.is-danger {
  margin-top: 10px;
}

.field {
  margin-bottom: 20px;
}
</style>
