const path = require('path');

console.log(process.env.NODE_ENV);

module.exports = {

    publicPath: '/',
    outputDir: 'dist',
    lintOnSave: true,
    runtimeCompiler: false,
    productionSourceMap: false,
    devServer: {
      port: 3000,
      https: false,
      hotOnly: false,
      proxy: {
        '/api': {
            target: process.env.BACKEND,
            // target: 'https://us-central1-linkkoub-prod.cloudfunctions.net/backend',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, ''),
            secure: true,

        }         
    },
      disableHostCheck: true,
    },
    

    configureWebpack: {
        devtool: 'source-map'
    },
    chainWebpack: config => {
        config.plugins.delete('prefetch');
    },
    pluginOptions: {
        'style-resources-loader': {
            preProcessor: 'scss',
            patterns: [
                       path.resolve(__dirname, 'src/assets/sass/app.scss')
                // Adicione caminhos para seus arquivos SCSS globais, se necessário
            ]
        },
        i18n: {
            locale: 'en',
            fallbackLocale: 'en',
            localeDir: 'locales',
            enableInSFC: false,
        },
    },
    chainWebpack: (config) => {
        // Remove prefetch plugin and that's it!
        config.plugins.delete('prefetch');
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@themeConfig': path.resolve(__dirname, 'theme.config.js'),
            },
            fallback: {

            },
        },
    }
};
