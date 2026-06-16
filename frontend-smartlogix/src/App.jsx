import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import Inventario from './pages/Inventario';
import Pedidos from './pages/Pedidos';
import NuevoPedido from './pages/NuevoPedido';
import './index.css';

function App() {
  return (
    <BrowserRouter>
      <div className="app">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/"             element={<Dashboard />} />
            <Route path="/inventario"   element={<Inventario />} />
            <Route path="/pedidos"      element={<Pedidos />} />
            <Route path="/nuevo-pedido" element={<NuevoPedido />} />
            <Route path="*"             element={<Navigate to="/" replace />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
