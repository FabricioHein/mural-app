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
        proxy: null,
        disableHostCheck: true,
    },

    configureWebpack: {
        devtool: 'source-map'
    },
    chainWebpack: config => {
        config.plugins.delete('prefetch');
    },

    chainWebpack: (config) => {
        // Remove prefetch plugin and that's it!
        config.plugins.delete('prefetch');
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@': path.resolve(__dirname, './src'),
            },
            fallback: {

            },
        },
    },
    devServer: {
        proxy: {
            '/api': {
                target: 'https://mural-app.onrender.com',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
                secure: true,

            }      
        },
    },
};
