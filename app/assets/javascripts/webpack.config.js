const path = require('path');
const babelEnvPreset = ['env', {
  "targets": {
    "browser" : ["last 2 versions"]
  }
}];

module.exports = {
  entry: './src/index.jsx',
  output: {
    filename: 'bundle.js'
  },
  module: {
    loaders: [
      { test: /\.js$/, loader: 'babel-loader', query: { presets: ['es2015', 'react', 'react-native'] } },
      { test: /\.jsx$/, loader: 'babel-loader' },
      { test: /\.scss$/,
        use: [{
                loader: "style-loader" // creates style nodes from JS strings
            }, {
                loader: "css-loader" // translates CSS into CommonJS
            }, {
                loader: "sass-loader" // compiles Sass to CSS
            }
          ]
      }
    ]
  }
};
