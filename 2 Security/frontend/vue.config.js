module.exports = {
  configureWebpack: {
    devServer: {
        proxy: {
          '/api': {
            target: 'https://localhost:8443',
            changeOrigin: true,
            ws: true,
            pathRewrite: {
              '^/api': ''
            }
          }
        }
      }
  }
};
