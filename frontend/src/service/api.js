import axios from 'axios';
import { useToast } from 'vue-toastification';

class ApiClient {
  constructor() {
    this.toast = useToast();
    this.api = axios.create({
      baseURL: 'https://mural-app.onrender.com',
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
  async get(url) {
    try {
      const response = await this.api.get(url, {
        headers: {
          'Accept': 'application/json', // Adicionando header de Aceitação
        },
      });

      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async post(url, data, msg) {
    try {
      const response = await this.api.post(url, data, {
        headers: {
          'Content-Type': 'application/json', // Confirmando o tipo de conteúdo JSON
        },
      });
      this.toast.success(msg || 'Dados enviados com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async postFormData(url, data) {
    try {
      const response = await this.api.post(url, data, {
        headers: {
          'Content-Type': 'multipart/form-data', // Para envio de FormData
        },
      });

      this.toast.success('Dados enviados com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async putFormData(url, data) {
    try {
      const response = await this.api.put(url, data, {
        headers: {
          'Content-Type': 'multipart/form-data', // Para envio de FormData
        },
      });

      this.toast.success('Dados enviados com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async put(url, data) {
    try {
      const response = await this.api.put(url, data, {
        headers: {
          'Content-Type': 'application/json', // Confirmando o tipo de conteúdo JSON
        },
      });
      this.toast.success('Dados atualizados com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async delete(url) {
    try {
      const response = await this.api.delete(url, {
        headers: {
          'Accept': 'application/json', // Adicionando header de Aceitação
        },
      });
      this.toast.success('Item excluído com sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async register(data) {
    try {
      await this.api.post('/api/auth/signup', data, {
        headers: {
          'Content-Type': 'application/json', // Confirmando o tipo de conteúdo JSON
        },
      });
      this.toast.success('Cadastrado com Sucesso!');
    } catch (error) {
      this.toast.error(error.message);
      this.handleError(error);
    }
  }

  async login(data) {
    try {
      const response = await this.api.post('/api/auth/signin', data, {
        headers: {
          'Content-Type': 'application/json', // Confirmando o tipo de conteúdo JSON
        },
      });
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
      console.error('Response data:', error.response.data);

      if (error.response.data.message) {
        this.toast.error(error.response.data.message);
      } else {
        this.toast.error(`Erro ${error.response.status}: Ocorreu um erro inesperado.`);
      }

      if (error.response.status === 401 && !error.response.data.message) {
        localStorage.removeItem('accessToken');
        this.toast.error('Error no acesso, tente novamente.');
        return; // Evita lançar o erro novamente após o tratamento
      }
    } else if (error.request) {
      console.error('Request data:', error.request);
      this.toast.error('Nenhuma resposta recebida do servidor.');
    } else {
      console.error('Error message:', error.message);
      this.toast.error(`Erro: ${error.message}`);
    }

    throw error;
  }
}

export default ApiClient;
