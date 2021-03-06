module.exports = {
  configureWebpack: {
    devServer: {
      proxy: {
        "/api": {
          target: "https://localhost:8443",
          changeOrigin: true,
          ws: true,
          pathRewrite: {
            "^/api": ""
          }
        }
      },
      https: true,
      headers: {
        "Access-Control-Allow-Origin": "https://localhost:3000",
        "Access-Control-Allow-Credentials": "true",
        "Access-Control-Allow-Methods":
          "GET, POST, PUT, DELETE, PATCH, OPTIONS",
        "Access-Control-Allow-Headers":
          "X-Requested-With, content-type, Authorization"
      }
    }
  }
};
