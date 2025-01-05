import axios from 'axios';
import { useToast } from 'vue-toastification';

class ApiClient {
  constructor() {
    this.toast = useToast();
    this.api = axios.create({
      baseURL: process.env.VUE_APP_API_URL || 'https://mural-app.onrender.com',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      withCredentials: true, // Importante para CORS com cookies
    });

    // Interceptor para adicionar o token Bearer e headers CORS
    this.api.interceptors.request.use(
      async (config) => {
        // Headers CORS padrão
        config.headers['Access-Control-Allow-Origin'] = '*';
        config.headers['Access-Control-Allow-Methods'] = 'GET,PUT,POST,DELETE,PATCH,OPTIONS';
        config.headers['Access-Control-Allow-Headers'] = 'Origin, X-Requested-With, Content-Type, Accept, Authorization';

        const token = await this.getToken();
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Interceptor para tratamento de respostas
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response && error.response.status === 0) {
          // CORS error
          this.toast.error('Erro de conexão com o servidor. Verifique se o CORS está configurado corretamente.');
        }
        return Promise.reject(error);
      }
    );
  }

  async getToken() {
    return localStorage.getItem('accessToken');
  }

  async get(url, params = {}) {
    try {
      const response = await this.api.get(url, { 
        params,
        headers: this.getHeaders() 
      });
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async post(url, data, msg) {
    try {
      const response = await this.api.post(url, data, {
        headers: this.getHeaders()
      });
      this.toast.success(msg || 'Dados enviados com sucesso!')
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  async postFormData(url, data) {
    try {
      const response = await this.api.post(url, data, {
        headers: {
          ...this.getHeaders(),
          'Content-Type': 'multipart/form-data',
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
          ...this.getHeaders(),
          'Content-Type': 'multipart/form-data',
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
        headers: this.getHeaders()
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
        headers: this.getHeaders()
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
        headers: this.getHeaders()
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
        headers: this.getHeaders()
      });
      this.toast.success('Logado com Sucesso!');
      return response.data;
    } catch (error) {
      this.handleError(error);
    }
  }

  // Método auxiliar para obter headers padrão
  getHeaders() {
    return {
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
      'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Authorization'
    };
  }

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
        return;
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