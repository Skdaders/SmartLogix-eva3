import React, { useEffect, useState } from 'react';
import { getProductos } from '../services/api';

function Inventario() {
  const [productos, setProductos] = useState([]);
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState(null);
  const [busqueda, setBusqueda]   = useState('');

  useEffect(() => {
    getProductos()
      .then(setProductos)
      .catch(e => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  const productosFiltrados = productos.filter(p =>
    p.nombre?.toLowerCase().includes(busqueda.toLowerCase()) ||
    p.codigo?.toLowerCase().includes(busqueda.toLowerCase())
  );

  if (loading) return <div className="loading"><div className="spinner" /><span>Cargando inventario...</span></div>;
  if (error)   return <div className="alert alert-warning">⚠️ {error}</div>;

  return (
    <div>
      <h1 className="page-title">
        Inventario
        <div className="page-subtitle">Gestión de productos y stock</div>
      </h1>

      <div className="section">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
          <h2 className="section-title" style={{ margin: 0 }}>
            Productos ({productosFiltrados.length})
          </h2>
          <input
            type="text"
            placeholder="Buscar por código o nombre..."
            value={busqueda}
            onChange={e => setBusqueda(e.target.value)}
            style={{ padding: '0.5rem 1rem', border: '1.5px solid #e2e8f0', borderRadius: '8px', fontSize: '0.9rem', width: '260px' }}
          />
        </div>

        {productosFiltrados.length === 0 ? (
          <div className="empty-state"><p>No se encontraron productos.</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th><th>Código</th><th>Nombre</th>
                  <th>Stock Actual</th><th>Stock Mínimo</th>
                  <th>Bodega</th><th>Precio</th><th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {productosFiltrados.map(p => (
                  <tr key={p.id}>
                    <td>{p.id}</td>
                    <td><strong>{p.codigo}</strong></td>
                    <td>{p.nombre}</td>
                    <td>
                      <span style={{ color: p.bajoStock ? '#ef4444' : '#10b981', fontWeight: 700 }}>
                        {p.stockActual}
                      </span>
                    </td>
                    <td>{p.stockMinimo}</td>
                    <td>Bodega {p.bodegaId}</td>
                    <td>${p.precioUnitario?.toFixed(2)}</td>
                    <td>
                      {p.bajoStock
                        ? <span className="badge badge-danger">BAJO STOCK</span>
                        : <span className="badge badge-confirmado">OK</span>
                      }
                    </td>
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

export default Inventario;
