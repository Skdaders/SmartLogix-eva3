import axios from 'axios';

// URL base del BFF (Backend For Frontend)
const BFF_URL = 'http://localhost:8080/bff';

const api = axios.create({
  baseURL: BFF_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
});

// ── Interceptor para manejar errores globalmente ──────────────────────────
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const mensaje = error.response?.data?.mensaje || 'Error al conectar con el servidor';
    return Promise.reject(new Error(mensaje));
  }
);

// ── Dashboard ─────────────────────────────────────────────────────────────
export const getDashboard = () => api.get('/dashboard').then(r => r.data);

// ── Productos ─────────────────────────────────────────────────────────────
export const getProductos = () => api.get('/productos').then(r => r.data);
export const getProductoDetalle = (id) => api.get(`/productos/${id}`).then(r => r.data);

// ── Pedidos ───────────────────────────────────────────────────────────────
export const crearPedido = (pedido) => api.post('/pedidos', pedido).then(r => r.data);
export const getPedidosPorCliente = (clienteId) =>
  api.get(`/pedidos/cliente/${clienteId}`).then(r => r.data);

export default api;
