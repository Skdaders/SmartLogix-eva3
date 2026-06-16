import React, { useState } from 'react';
import { getPedidosPorCliente } from '../services/api';

function Pedidos() {
  const [clienteId, setClienteId] = useState('');
  const [pedidos, setPedidos]     = useState([]);
  const [loading, setLoading]     = useState(false);
  const [error, setError]         = useState(null);
  const [buscado, setBuscado]     = useState(false);

  const buscarPedidos = () => {
    if (!clienteId.trim()) return;
    setLoading(true);
    setError(null);
    getPedidosPorCliente(clienteId.trim())
      .then(data => { setPedidos(data); setBuscado(true); })
      .catch(e => setError(e.message))
      .finally(() => setLoading(false));
  };

  const handleKeyDown = (e) => { if (e.key === 'Enter') buscarPedidos(); };

  const getBadgeClase = (tipo) => `badge badge-${tipo?.toLowerCase()}`;
  const getEstadoClase = (estado) => `badge badge-${estado?.toLowerCase()}`;

  return (
    <div>
      <h1 className="page-title">
        Pedidos
        <div className="page-subtitle">Consulta pedidos por cliente</div>
      </h1>

      {/* Buscador */}
      <div className="section">
        <h2 className="section-title">Buscar pedidos</h2>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
          <div className="form-group" style={{ flex: 1, margin: 0 }}>
            <label>ID del Cliente</label>
            <input
              type="text"
              placeholder="Ej: CLI-001"
              value={clienteId}
              onChange={e => setClienteId(e.target.value)}
              onKeyDown={handleKeyDown}
            />
          </div>
          <button className="btn btn-primary" onClick={buscarPedidos} disabled={loading || !clienteId.trim()}>
            {loading ? 'Buscando...' : 'Buscar'}
          </button>
        </div>
      </div>

      {error && <div className="alert alert-error">⚠️ {error}</div>}

      {/* Resultados */}
      {buscado && (
        <div className="section">
          <h2 className="section-title">
            Pedidos del cliente <strong>{clienteId}</strong> ({pedidos.length})
          </h2>

          {pedidos.length === 0 ? (
            <div className="empty-state"><p>No se encontraron pedidos para este cliente.</p></div>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>ID</th><th>Producto</th><th>Cantidad</th>
                    <th>Tipo</th><th>Total</th><th>Días</th>
                    <th>Estado</th><th>Fecha</th>
                  </tr>
                </thead>
                <tbody>
                  {pedidos.map(p => (
                    <tr key={p.id}>
                      <td>#{p.id}</td>
                      <td>Prod. #{p.productoId}</td>
                      <td>{p.cantidad}</td>
                      <td><span className={getBadgeClase(p.tipo)}>{p.tipo}</span></td>
                      <td><strong>${p.precioTotal?.toFixed(2)}</strong></td>
                      <td>{p.diasEntrega}d</td>
                      <td><span className={getEstadoClase(p.estado)}>{p.estado}</span></td>
                      <td>{p.fechaCreacion ? new Date(p.fechaCreacion).toLocaleDateString('es-CL') : '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default Pedidos;
