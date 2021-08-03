const fs = require('fs')
module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    https: {
      key: fs.readFileSync('./cert/example.com+5-key.pem'),
      cert: fs.readFileSync('./cert/example.com+5.pem'),
    },
    proxy: {
      '/api/v1': {
        target: 'https://localhost:8080'
      }
    },
    historyApiFallback: true,
  }
};
