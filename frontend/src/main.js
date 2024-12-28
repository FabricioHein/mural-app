import { createApp } from 'vue'
import './bulma.min.css'
import './style.css'

// src/main.js
import App from './App.vue';
import router from './router/index'; // Importando o Vue Router

import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';

console.log(router)

createApp(App)
  .use(router)
  .use(Toast, {
    position: "top-right",
    timeout: 5000,
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: false,
    hideProgressBar: false,
    closeButton: "button",
    icon: true,
  })
  .mount('#app');