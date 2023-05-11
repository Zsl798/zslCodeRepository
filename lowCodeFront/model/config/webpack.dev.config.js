var webpack = require('webpack');
var path = require('path');
var envConfig = require('./env');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var MiniCssExtractPlugin = require('mini-css-extract-plugin');
var ScriptExtHtmlPlugin = require('script-ext-html-webpack-plugin');

module.exports = {
  mode: 'development',
  devtool: 'cheap-module-eval-source-map',
  entry: {
    app: [
      // require.resolve("@babel/polyfill"),
      path.resolve(__dirname, '../src/lib/Math'),
      path.resolve(__dirname, '../src/index'),
    ],
  },
  //优化项配置
  optimization: {
    // 分割代码块
    splitChunks: {
      cacheGroups: {
        //公用模块抽离
        common: {
          //提取公共文件并打包
          name: 'common',
          chunks: 'all',
          minSize: 1, //大于0个字节
          priority: 0, //权重
        },
        //第三方库抽离
        vendor: {
          name: 'vendor',
          test: /[\\/]node_modules[\\/]/,
          chunks: 'all',
          priority: 10,
        },
      },
    },
  },
  output: {
    path: path.resolve(__dirname, '../build'),
    filename: '[name].js',
  },
  plugins: [
    new HtmlWebpackPlugin({
      hash: true,
      inject: true,
      template: path.resolve(__dirname, '../public/index.html'),
      filename: 'index.html',
      chunks: ['vendor', 'common', 'app'],
    }),
    new ScriptExtHtmlPlugin({
      defaultAttribute: 'defer',
    }),
    new MiniCssExtractPlugin({
      filename: '[name].style.css',
    }),
    new webpack.DefinePlugin({
      envConfig: JSON.stringify(envConfig),
    }),
  ],
  resolveLoader: {
    modules: ['node_modules', 'config'],
  },
  resolve: {
    mainFields: ['browser', 'main'],
    alias: {
      components: path.resolve(__dirname, '../src/components'),
      style: path.resolve(__dirname, '../src/style/index.less'),
    },
  },
  module: {
    rules: [
      {
        test: /\.(js|tsx|jsx)$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
      },
      {
        test: /\.(js|tsx|jsx)$/,
        exclude: /node_modules/,
        //loader: 'eslint-loader',
      },
      {
        test: /\.(css|less)$/,
        loader: [
          {
            loader: MiniCssExtractPlugin.loader,
          },
          'css-loader',
          {
            loader: 'postcss-loader',
            options: { plugins: () => [require('autoprefixer')()] },
          },
          { loader: 'less-loader', options: { javascriptEnabled: true } },
        ],
      },
      {
        test: /\.(png|jpg|svg|gif)$/,
        loader: 'url-loader',
        options: {
          limit: 8192,
        },
      },
      // {
      //   //把MiddleLoader.js作用于../src/lib/middle.js文件,并传入参数platform=json
      //   //MiddleLoader.js的作用: 由传入的参数来决定实际加载的文件
      //   test: require.resolve("../src/lib/middle"),
      //   loader: "MiddleLoader?platform=json",
      // },
    ],
  },
  // target: "electron-renderer",
};
