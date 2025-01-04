<template>
  <div class="container" v-if="load">
    <h2 class="title has-text-centered">Login</h2>
    <form @submit.prevent="handleSubmit" class="box">
      <div class="field">
        <label class="label" for="email">Email</label>
        <div class="control">
          <input class="input" type="text" id="email" v-model="formData.email" required
            placeholder="Digite o seu email" />
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
        email: '',
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
    async handleSubmit() {


      this.passwordError = false; // Limpa o erro, caso a senha seja válida

      const apiClient = new ApiClient();
      try {
        const response = await apiClient.login(this.formData);

        // Armazenar o accessToken no localStorage
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('email', response.email);
        localStorage.setItem('userId', response.id);
        localStorage.setItem('roles', response.roles[0]);

        // Redirecionar para a página inicial ou página protegida
        if (response.roles[0] == 'ROLE_USER' && response.weddingData.uuid != 'novios' && this.weddingData.uuid) {
          this.$router.push({
            name: 'Menu',
            query: {
              uuid: this.weddingData.uuid
            }
          });
          return
        }

        if (response.roles[0] == 'ROLE_MODERATOR' && response.weddingData.uuid) {
          this.$router.push({
            name: 'Menu',
            query: {
              uuid: response.weddingData.uuid
            }
          });
          return


        }else{
          this.$router.push({
            name: 'dados-casamento',
            query: {
              uuid: 'noivos'
            }
          });
          return

        }

      } catch (error) {
        console.error('Erro ao realizar login:', error);

        // Se ocorrer erro, limpar os dados do localStorage
        localStorage.removeItem('accessToken');
        localStorage.removeItem('email');
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
