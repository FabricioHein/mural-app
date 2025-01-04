import { createApp } from 'vue'
import './bulma.min.css'
import './style.css'

// src/main.js
import App from './App.vue';
import router from './router/index'; // Importando o Vue Router

import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';

// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDxyVw0SlWjYvkC3ZEoiAPHE6X9CD2xVlI",
  authDomain: "mural-casamento-noivos.firebaseapp.com",
  projectId: "mural-casamento-noivos",
  storageBucket: "mural-casamento-noivos.firebasestorage.app",
  messagingSenderId: "574901849370",
  appId: "1:574901849370:web:e9bfc76a4d4fb2002eb0a3",
  measurementId: "G-77K6V3VMF4"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);


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