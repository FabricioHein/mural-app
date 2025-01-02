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
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCAezKpQG24yNBn5b8kTi0qjQz-j3kotew",
  authDomain: "muradodosnoivos.firebaseapp.com",
  projectId: "muradodosnoivos",
  storageBucket: "muradodosnoivos.firebasestorage.app",
  messagingSenderId: "867390530535",
  appId: "1:867390530535:web:b918007862ff2fea31d342",
  measurementId: "G-GQK2DPTG2P"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);


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