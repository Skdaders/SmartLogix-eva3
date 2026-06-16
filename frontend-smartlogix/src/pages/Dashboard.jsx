import React, { useEffect, useState } from 'react';
import { getDashboard } from '../services/api';

function Dashboard() {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState(null);

  useEffect(() => {
    getDashboard()
      .then(setData)
      .catch(e => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading"><div className="spinner" /><span>Cargando dashboard...</span></div>;
  if (error)   return <div className="alert alert-warning">⚠️ No se pudo conectar con el servidor. Verifica que los microservicios estén corriendo.</div>;

  const bajoStock    = data?.productosBajoStock  || [];
  const pedidos      = data?.pedidosRecientes    || [];
  const totalProd    = data?.totalProductos      ?? 0;
  const totalPedidos = data?.totalPedidos        ?? 0;

  return (
    <div>
      <h1 className="page-title">
        Dashboard
        <div className="page-subtitle">Resumen general de SmartLogix</div>
      </h1>

      {/* Estadísticas */}
      <div className="stats-grid">
        <div className="stat-card success">
          <span className="stat-label">Total Productos</span>
          <span className="stat-value">{totalProd}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Total Pedidos</span>
          <span className="stat-value">{totalPedidos}</span>
        </div>
        <div className={`stat-card ${bajoStock.length > 0 ? 'danger' : 'success'}`}>
          <span className="stat-label">Bajo Stock</span>
          <span className="stat-value">{bajoStock.length}</span>
        </div>
        <div className="stat-card warning">
          <span className="stat-label">Pedidos Pendientes</span>
          <span className="stat-value">
            {pedidos.filter(p => p.estado === 'PENDIENTE').length}
          </span>
        </div>
      </div>

      {/* Alertas bajo stock */}
      {bajoStock.length > 0 && (
        <div className="alert alert-error">
          ⚠️ Hay <strong>{bajoStock.length}</strong> producto(s) con stock bajo el mínimo. Revisa el módulo de Inventario.
        </div>
      )}

      {/* Tabla productos bajo stock */}
      <div className="section">
        <h2 className="section-title">⚠️ Productos con Bajo Stock</h2>
        {bajoStock.length === 0 ? (
          <div className="empty-state"><p>✅ Todos los productos tienen stock suficiente.</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr><th>Código</th><th>Nombre</th><th>Stock Actual</th><th>Stock Mínimo</th><th>Estado</th></tr>
              </thead>
              <tbody>
                {bajoStock.map(p => (
                  <tr key={p.id}>
                    <td>{p.codigo}</td>
                    <td>{p.nombre}</td>
                    <td><strong style={{ color: '#ef4444' }}>{p.stockActual}</strong></td>
                    <td>{p.stockMinimo}</td>
                    <td><span className="badge badge-danger">CRÍTICO</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Pedidos recientes */}
      <div className="section">
        <h2 className="section-title">📦 Pedidos Recientes</h2>
        {pedidos.length === 0 ? (
          <div className="empty-state"><p>No hay pedidos registrados aún.</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr><th>ID</th><th>Cliente</th><th>Tipo</th><th>Total</th><th>Días Entrega</th><th>Estado</th></tr>
              </thead>
              <tbody>
                {pedidos.slice(0, 10).map(p => (
                  <tr key={p.id}>
                    <td>#{p.id}</td>
                    <td>{p.clienteId}</td>
                    <td><span className={`badge badge-${p.tipo?.toLowerCase()}`}>{p.tipo}</span></td>
                    <td>${p.precioTotal?.toFixed(2)}</td>
                    <td>{p.diasEntrega}d</td>
                    <td><span className={`badge badge-${p.estado?.toLowerCase()}`}>{p.estado}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
