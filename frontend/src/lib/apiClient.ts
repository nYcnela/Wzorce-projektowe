import axios from "axios";

// Create Axios instance
const apiClient = axios.create({
  baseURL: "http://localhost:8080/api", // Backend URL
});

// Request Interceptor: Attach Token
apiClient.interceptors.request.use(
  (config) => {
    // Only access localStorage in the browser
    if (typeof window !== "undefined") {
      const token = localStorage.getItem("jwt_token");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Handle Errors (e.g., 401/403)
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (
      error.response &&
      (error.response.status === 401 || error.response.status === 403)
    ) {
      // Clear token and redirect to login if token is invalid/expired
      if (typeof window !== "undefined") {
        localStorage.removeItem("jwt_token");
        // Optional: Redirect to login page
        // window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;
