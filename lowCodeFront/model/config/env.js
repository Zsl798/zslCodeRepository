const env = process.env.NODE_ENV;
const config = {
  development: {
    REACT_APP_BASE_API: '/api',
  },
  production: {
    REACT_APP_BASE_API: '/api',
  },
};
module.exports = config[env];
