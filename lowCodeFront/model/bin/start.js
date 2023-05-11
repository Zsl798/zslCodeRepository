process.env.NODE_ENV = 'development';

var path = require('path');
var childProcess = require('child_process');
var webpack = require('webpack');
var config = require('../config/webpack.dev.config.js');

var profile = require('../profile');
var host = profile.host;
var port = profile.port;
var protocol = profile.protocol;

var WebpackDevServer = require('webpack-dev-server');

config.entry.app.unshift(
  `webpack-dev-server/client?${protocol}://${host}:${port}/`
);
//http://10.14.1.14:9528/bog/api/projectInfo/queryProjectListByPage
var compiler = webpack(config);
const bdapServerURL = 'http://localhost:8881'; // zsl 后端
// const bdapServerURL = 'http://10.11.51.174:9090'; // sg 后端
//init server
var devServer = new WebpackDevServer(compiler, {
  stats: { colors: true },
  contentBase: path.resolve(__dirname, '../public'), //启动服务后访问该目录下的html
  proxy: {
    '/api': {
      target: bdapServerURL,
      //pathRewrite: { '^/bog/api': '' },
    },
  },
//  open: true,
});

// devServer.listen(port, host, function () {
//   // 启动electron
//   childProcess
//     .spawn("npm", ["run", "electron"], {
//       shell: true,
//       env: process.env,
//       stdio: "inherit",
//     })
//     .on("close", (code) => process.exit(code))
//     .on("error", (spawnError) => console.error(spawnError));
// });

devServer.listen(port, host, function (err) {
  if (err) {
    console.log('err', err);
  }
});

console.log(`listen at ${protocol}://${host}:${port}`);
