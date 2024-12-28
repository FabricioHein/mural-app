<!-- src/components/CadastroNoiva.vue -->
<template>
    <div class="container">
      <h2 class="title has-text-centered">Cadastro Noiva</h2>
      <form @submit.prevent="handleSubmit" class="box">
        <div class="field">
          <label class="label" for="username">Nome</label>
          <div class="control">
            <input
              class="input"
              type="text"
              id="username"
              v-model="formData.username"
              required
              placeholder="Digite o nome da noiva"
            />
          </div>
        </div>
  
        <div class="field">
          <label class="label" for="email">Email</label>
          <div class="control">
            <input
              class="input"
              type="email"
              id="email"
              v-model="formData.email"
              required
              placeholder="Digite o email da noiva"
            />
          </div>
        </div>
  
        <div class="field">
          <label class="label" for="password">Senha</label>
          <div class="control">
            <input
              class="input"
              type="password"
              id="password"
              v-model="formData.password"
              required
              placeholder="Digite a senha"
            />
          </div>
          <!-- Exibindo a mensagem de erro se a senha for menor que 4 caracteres -->
          <p v-if="passwordError" class="help is-danger">A senha deve ter mais de 4 caracteres.</p>
        </div>
  
        <div class="field is-grouped is-grouped-centered">
          <div class="control">
            <button type="submit" class="button is-primary" :disabled="formData.password.length <= 4">Cadastrar Noiva</button>
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
          email: '',
          password: '',
          role: ['moderator'],
        },
        passwordError: false,
      };
    },
    methods: {
      async handleSubmit() {
        if (this.formData.password.length <= 4) {
          this.passwordError = true; // Exibe o erro
          return;
        }
        
        this.passwordError = false; // Limpa o erro, caso a senha seja válida
  
        const apiClient = new ApiClient();
        try {
          const response = await apiClient.register(this.formData);
          console.log(response);
          this.resetForm();
        } catch (error) {
        }
      },
      resetForm() {
        this.formData = {
          username: '',
          email: '',
          password: '',
          role: ['moderator'],
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
  </style>
  