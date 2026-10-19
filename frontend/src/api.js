import axios from 'axios';

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
});


// We inject logout from AuthContext
let logout = null;
let isLoggingOut = false;

export const setLogoutHandler = (logoutFn) => {
  logout = logoutFn;
};

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (
      error.response?.status === 401 &&
      logout &&
      !isLoggingOut
    ) {
      isLoggingOut = true;
      logout();
    }
    return Promise.reject(error);
  }
);

// Attach token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle expired / invalid JWT
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      if (logout) {
        logout();
      }
    }
    return Promise.reject(error);
  }
);

export default api;
