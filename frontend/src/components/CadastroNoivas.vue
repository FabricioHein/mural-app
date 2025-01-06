<template>
  <div class="container" v-if="load" >
    <h2 class="title has-text-centered">Cadastro Noivos</h2>

    <!-- Link para quem já tem cadastro -->
    <div class="has-text-centered">
      <p>Já tem cadastro? <router-link :to="{ name: 'Login', query: { uuid: 'noivos' } }"
          class="has-text-link">Faça login</router-link></p>
    </div>

    <form @submit.prevent="handleSubmit" class="box">
      <div class="field">
        <div class="field">
        <label class="label" for="name">Nome</label>
        <div class="control">
          <input class="input" type="text" id="username" v-model="formData.name" required
            placeholder="Digite o nome do convidado" />
        </div>
      </div>
        <label class="label" for="username">Usuário</label>
        <div class="control">
          <input class="input" type="text" id="username" v-model="formData.username" required
            placeholder="Digite um usuário" />
        </div>
      </div>

      <div class="field">
        <label class="label" for="email">Email</label>
        <div class="control">
          <input class="input" type="email" id="email" v-model="formData.email" required
            placeholder="Digite o email do convidado" />
        </div>
      </div>

      <div class="field">
        <label class="label" for="password">Senha</label>
        <div class="control">
          <input class="input" type="password" id="password" v-model="formData.password" required
            placeholder="Digite a senha" />
        </div>
        <div class="field">
          <label class="label">Tipo de Cadastro</label>
          <div class="control">
            <label class="radio">
              <input type="radio" v-model="formData.role[0]" value="mod">
              Noivos
            </label>           
          </div>
        </div>

        <!-- Exibindo a mensagem de erro se a senha for menor que 4 caracteres -->
        <p v-if="passwordError" class="help is-danger">A senha deve ter mais de 4 caracteres.</p>
      </div>

      <div class="field is-grouped is-grouped-centered">
        <div class="control">
          <button type="submit" class="button is-primary" :disabled="formData.password.length <= 4">Cadastrar
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
      data: {},
      formData: {
        name: '',
        username: '',
        email: '',
        password: '',
        role: ['mod'],
      },
      load: false,
      weddingData: {
        uuid: null,
        id: null
      },
      passwordError: false,
    };
  },
 async mounted() {
  await  this.fetchWeddingData();
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
      if (this.formData.password.length <= 4) {
        this.passwordError = true; // Exibe o erro
        return;
      }

      this.passwordError = false; // Limpa o erro, caso a senha seja válida

      const apiClient = new ApiClient();
      try {

       await apiClient.register(this.formData);

        this.resetForm();
      } catch (error) {
        console.error("Erro ao realizar o cadastro:", error);
      }
    },
    resetForm() {
      this.formData = {
        username: '',
        email: '',
        password: '',
        role: ['mod'],
      };
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

.has-text-center {
  text-align: center;
}
</style>
