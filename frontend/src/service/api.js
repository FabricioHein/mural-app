import axios from 'axios';
import { useToast } from 'vue-toastification';

class ApiClient {
  constructor() {
    this.toast = useToast();
    this.api = axios.create({
      baseURL: 'http://localhost:8080',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Interceptor para adicionar o token Bearer automaticamente
    this.api.interceptors.request.use(
      async (config) => {
        const token = await this.getToken(); // Obtém o token (implemente a lógica para obtê-lo)
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  }

  // Método para definir como o token será obtido
  async getToken() {
    // Substitua pela lógica para obter o token (ex.: localStorage, sessionStorage, API, etc.)
    return localStorage.getItem('accessToken');
  }

  // Métodos para as requisições
  async get(url, params = {}) {
    try {
      const response = await this.api.get(url, { params });

      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async post(url, data) {
    try {
      const response = await this.api.post(url, data);
      this.toast.success('Dados enviados com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async put(url, data) {
    try {
      const response = await this.api.put(url, data);
      this.toast.success('Dados atualizados com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async delete(url) {
    try {
      const response = await this.api.delete(url);
      this.toast.success('Item excluído com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }
  async register(data) {
    try {
      await this.api.post('/api/auth/signup', data);
      this.toast.success('Cadastrado com Sucesso!');
    } catch (error) {
      this.toast.error(error.message);
      this.handleError(error);
    }
  }
  async login(data) {
    try {
      const response = await this.api.post('/api/auth/signin', data);
      this.toast.success('Logado com Sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  // Método para tratar erros
  handleError(error) {
    console.error('API Error:', error);
    if (error.response) {
      // Resposta de erro da API
      console.error('Response data:', error.response.data);
      console.error('Response status:', error.response.status);
      console.error('Response headers:', error.response.headers);
  
      if (error.response.status === 401) {
        // Remove o token e atualiza a página
        // localStorage.removeItem('accessToken');
        // this.toast.error('Sessão expirada. Faça login novamente.');
        // window.location.reload(); // Atualiza a página
        return; // Evita lançar o erro novamente após o tratamento
      }
  
      if (error.message) {
        this.toast.error(error.message);
      } else {
        this.toast.error(`Erro ${error.response.status}: ${error.response || 'Ocorreu um erro.'}`);
      }
    } else if (error.request) {
      // Nenhuma resposta recebida
      console.error('Request data:', error.request);
      this.toast.error('Nenhuma resposta recebida do servidor.');
    } else {
      // Erro ao configurar a requisição
      console.error('Error message:', error.message);
      this.toast.error(`Erro: ${error.message}`);
    }
    throw error;
  }
}

export default ApiClient;
