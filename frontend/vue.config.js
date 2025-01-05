const path = require('path');

// Verifique o ambiente no qual o Node.js está executando
console.log(process.env.NODE_ENV);

module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  lintOnSave: true,
  runtimeCompiler: false,
  productionSourceMap: false,

  devServer: {
    port: 8080,
    https: false,
    hotOnly: false,
    proxy: {
      '/api': {
        target: 'https://mural-app.onrender.com',  // Usando variável de ambiente para o target
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        secure: false,
      }
      
    },
    disableHostCheck: true, // Desabilita a verificação de host
  },

  configureWebpack: {
    devtool: 'source-map',
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'), // Usando alias corretamente para simplificar os caminhos de importação
      },
      fallback: {},
    },
  },

  chainWebpack: (config) => {
    // Remove o plugin 'prefetch', que pode não ser necessário para seu caso
    config.plugins.delete('prefetch');
  },

  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'scss',
      patterns: [
        path.resolve(__dirname, 'src/assets/sass/app.scss'), // Caminho para o SCSS global
      ],
    },
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
    },
  },
};
