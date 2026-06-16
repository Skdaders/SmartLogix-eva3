import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';

// ── Mock del módulo api para no llamar al BFF real ─────────────────────
jest.mock('../services/api', () => ({
  getDashboard: jest.fn(),
  getProductos: jest.fn(),
  crearPedido: jest.fn(),
  getPedidosPorCliente: jest.fn(),
}));

import * as api from '../services/api';
import { BrowserRouter } from 'react-router-dom';

import Dashboard   from '../pages/Dashboard';
import Inventario  from '../pages/Inventario';
import NuevoPedido from '../pages/NuevoPedido';
import Pedidos     from '../pages/Pedidos';
import Navbar      from '../components/Navbar';

// Helper para envolver con Router
const renderWithRouter = (ui) =>
  render(<BrowserRouter>{ui}</BrowserRouter>);

// ── NAVBAR ─────────────────────────────────────────────────────────────

describe('Navbar', () => {
  test('renderiza los enlaces de navegación', () => {
    renderWithRouter(<Navbar />);
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Inventario')).toBeInTheDocument();
    expect(screen.getByText('Pedidos')).toBeInTheDocument();
    expect(screen.getByText('Nuevo Pedido')).toBeInTheDocument();
  });

  test('muestra la marca SmartLogix', () => {
    renderWithRouter(<Navbar />);
    expect(screen.getByText(/smart/i)).toBeInTheDocument();
  });
});

// ── DASHBOARD ──────────────────────────────────────────────────────────

describe('Dashboard', () => {
  test('muestra spinner de carga inicialmente', () => {
    api.getDashboard.mockReturnValue(new Promise(() => {})); // promesa pendiente
    renderWithRouter(<Dashboard />);
    expect(screen.getByText(/cargando/i)).toBeInTheDocument();
  });

  test('muestra estadísticas después de cargar datos', async () => {
    api.getDashboard.mockResolvedValue({
      productosBajoStock: [],
      pedidosRecientes: [
        { id: 1, clienteId: 'CLI-001', tipo: 'NORMAL', precioTotal: 200, diasEntrega: 5, estado: 'PENDIENTE' }
      ],
      totalProductos: 15,
      totalPedidos: 3,
    });
    renderWithRouter(<Dashboard />);
    await waitFor(() => expect(screen.getByText('15')).toBeInTheDocument());
    expect(screen.getByText('3')).toBeInTheDocument();
  });

  test('muestra alerta cuando hay productos bajo stock', async () => {
    api.getDashboard.mockResolvedValue({
      productosBajoStock: [
        { id: 1, codigo: 'P-001', nombre: 'Laptop', stockActual: 2, stockMinimo: 10 }
      ],
      pedidosRecientes: [],
      totalProductos: 5,
      totalPedidos: 0,
    });
    renderWithRouter(<Dashboard />);
    await waitFor(() => expect(screen.getByText(/bajo stock/i)).toBeInTheDocument());
  });

  test('muestra mensaje de error si falla la API', async () => {
    api.getDashboard.mockRejectedValue(new Error('Error de conexión'));
    renderWithRouter(<Dashboard />);
    await waitFor(() => expect(screen.getByText(/microservicios/i)).toBeInTheDocument());
  });

  test('muestra mensaje cuando no hay productos bajo stock', async () => {
    api.getDashboard.mockResolvedValue({
      productosBajoStock: [],
      pedidosRecientes: [],
      totalProductos: 10,
      totalPedidos: 0,
    });
    renderWithRouter(<Dashboard />);
    await waitFor(() => expect(screen.getByText(/stock suficiente/i)).toBeInTheDocument());
  });
});

// ── INVENTARIO ─────────────────────────────────────────────────────────

describe('Inventario', () => {
  test('muestra spinner mientras carga', () => {
    api.getProductos.mockReturnValue(new Promise(() => {}));
    renderWithRouter(<Inventario />);
    expect(screen.getByText(/cargando inventario/i)).toBeInTheDocument();
  });

  test('renderiza tabla de productos correctamente', async () => {
    api.getProductos.mockResolvedValue([
      { id: 1, codigo: 'PROD-001', nombre: 'Laptop Dell', stockActual: 50, stockMinimo: 10, bodegaId: 1, precioUnitario: 999.99, bajoStock: false },
      { id: 2, codigo: 'PROD-002', nombre: 'Mouse', stockActual: 5, stockMinimo: 20, bodegaId: 1, precioUnitario: 25.00, bajoStock: true },
    ]);
    renderWithRouter(<Inventario />);
    await waitFor(() => expect(screen.getByText('Laptop Dell')).toBeInTheDocument());
    expect(screen.getByText('Mouse')).toBeInTheDocument();
    expect(screen.getByText('PROD-001')).toBeInTheDocument();
  });

  test('filtra productos por búsqueda', async () => {
    api.getProductos.mockResolvedValue([
      { id: 1, codigo: 'PROD-001', nombre: 'Laptop Dell', stockActual: 50, stockMinimo: 10, bodegaId: 1, precioUnitario: 999.99, bajoStock: false },
      { id: 2, codigo: 'PROD-002', nombre: 'Mouse Logitech', stockActual: 100, stockMinimo: 20, bodegaId: 1, precioUnitario: 25.00, bajoStock: false },
    ]);
    renderWithRouter(<Inventario />);
    await waitFor(() => screen.getByText('Laptop Dell'));
    const input = screen.getByPlaceholderText(/buscar/i);
    fireEvent.change(input, { target: { value: 'Mouse' } });
    expect(screen.queryByText('Laptop Dell')).not.toBeInTheDocument();
    expect(screen.getByText('Mouse Logitech')).toBeInTheDocument();
  });

  test('muestra badge BAJO STOCK cuando corresponde', async () => {
    api.getProductos.mockResolvedValue([
      { id: 1, codigo: 'P-01', nombre: 'Cable', stockActual: 2, stockMinimo: 10, bodegaId: 1, precioUnitario: 5.0, bajoStock: true },
    ]);
    renderWithRouter(<Inventario />);
    await waitFor(() => expect(screen.getByText('BAJO STOCK')).toBeInTheDocument());
  });
});

// ── NUEVO PEDIDO ───────────────────────────────────────────────────────

describe('NuevoPedido', () => {
  test('renderiza el formulario correctamente', () => {
    renderWithRouter(<NuevoPedido />);
    expect(screen.getByPlaceholderText(/CLI-001/i)).toBeInTheDocument();
    expect(screen.getByText(/Crear Pedido/i)).toBeInTheDocument();
  });

  test('muestra error si campos vacíos', async () => {
    renderWithRouter(<NuevoPedido />);
    fireEvent.click(screen.getByText(/Crear Pedido/i));
    await waitFor(() => expect(screen.getByText(/completa todos los campos/i)).toBeInTheDocument());
  });

  test('muestra precio estimado al ingresar datos', () => {
    renderWithRouter(<NuevoPedido />);
    fireEvent.change(screen.getByPlaceholderText(/Ej: 5/), { target: { value: '2' } });
    fireEvent.change(screen.getByPlaceholderText(/99.99/), { target: { value: '100' } });
    expect(screen.getByText(/precio estimado/i)).toBeInTheDocument();
  });

  test('crea pedido exitosamente con datos válidos', async () => {
    api.crearPedido.mockResolvedValue({ id: 7, precioTotal: 200.0 });
    renderWithRouter(<NuevoPedido />);
    fireEvent.change(screen.getByPlaceholderText(/CLI-001/i), { target: { value: 'CLI-001' } });
    fireEvent.change(screen.getByPlaceholderText(/Ej: 1/),    { target: { value: '1' } });
    fireEvent.change(screen.getByPlaceholderText(/Ej: 5/),    { target: { value: '2' } });
    fireEvent.change(screen.getByPlaceholderText(/99.99/),    { target: { value: '100' } });
    fireEvent.click(screen.getByText(/Crear Pedido/i));
    await waitFor(() => expect(screen.getByText(/Pedido #7/i)).toBeInTheDocument());
  });

  test('tabla de referencia muestra los 3 tipos de pedido', () => {
    renderWithRouter(<NuevoPedido />);
    expect(screen.getByText('NORMAL')).toBeInTheDocument();
    expect(screen.getByText('URGENTE')).toBeInTheDocument();
    expect(screen.getByText('MAYORISTA')).toBeInTheDocument();
  });
});

// ── PEDIDOS ────────────────────────────────────────────────────────────

describe('Pedidos', () => {
  test('renderiza el buscador', () => {
    renderWithRouter(<Pedidos />);
    expect(screen.getByPlaceholderText(/CLI-001/i)).toBeInTheDocument();
    expect(screen.getByText('Buscar')).toBeInTheDocument();
  });

  test('botón deshabilitado si no hay clienteId', () => {
    renderWithRouter(<Pedidos />);
    expect(screen.getByText('Buscar')).toBeDisabled();
  });

  test('habilita botón al ingresar clienteId', () => {
    renderWithRouter(<Pedidos />);
    fireEvent.change(screen.getByPlaceholderText(/CLI-001/i), { target: { value: 'CLI-001' } });
    expect(screen.getByText('Buscar')).not.toBeDisabled();
  });

  test('muestra pedidos al buscar', async () => {
    api.getPedidosPorCliente.mockResolvedValue([
      { id: 1, productoId: 1, cantidad: 2, tipo: 'NORMAL', precioTotal: 200, diasEntrega: 5, estado: 'PENDIENTE', fechaCreacion: '2026-06-01' }
    ]);
    renderWithRouter(<Pedidos />);
    fireEvent.change(screen.getByPlaceholderText(/CLI-001/i), { target: { value: 'CLI-001' } });
    fireEvent.click(screen.getByText('Buscar'));
    await waitFor(() => expect(screen.getByText('#1')).toBeInTheDocument());
  });

  test('muestra mensaje si no hay pedidos para el cliente', async () => {
    api.getPedidosPorCliente.mockResolvedValue([]);
    renderWithRouter(<Pedidos />);
    fireEvent.change(screen.getByPlaceholderText(/CLI-001/i), { target: { value: 'CLI-999' } });
    fireEvent.click(screen.getByText('Buscar'));
    await waitFor(() => expect(screen.getByText(/no se encontraron pedidos/i)).toBeInTheDocument());
  });
});
