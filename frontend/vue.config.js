const path = require('path');

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
        target: 'https://mural-app.onrender.com',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        },
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
          'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Authorization'
        },
        onProxyRes: function(proxyRes) {
          proxyRes.headers['Access-Control-Allow-Origin'] = '*';
        }
      }
    },
    allowedHosts: 'all', // Substitui o disableHostCheck que está depreciado
  },

  configureWebpack: {
    devtool: 'source-map',
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
      },
      fallback: {},
    },
  },

  chainWebpack: (config) => {
    config.plugins.delete('prefetch');
  },

  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'scss',
      patterns: [
        path.resolve(__dirname, 'src/assets/sass/app.scss'),
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