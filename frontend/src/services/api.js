import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const stored = localStorage.getItem('user');
  if (stored) {
    try {
      const user = JSON.parse(stored);
      if (user && user.id) {
        config.headers['X-User-Id'] = user.id;
      }
    } catch (e) {
      // ignore parse errors
    }
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      const message = error.response.data?.message;
      if (message) {
        error.message = message;
      }
    } else if (error.request) {
      error.message = 'Cannot reach the server. Ensure the backend is running on port 8082.';
    }
    return Promise.reject(error);
  }
);

export default api;
