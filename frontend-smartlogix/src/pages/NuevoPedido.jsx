import React, { useState } from 'react';
import { crearPedido } from '../services/api';

const TIPOS_PEDIDO = [
  { value: 'NORMAL',    label: 'Normal — 5 días, sin recargo' },
  { value: 'URGENTE',   label: 'Urgente — 1 día, +15%' },
  { value: 'MAYORISTA', label: 'Mayorista — 7 días, -20%' },
];

const FORM_INICIAL = {
  clienteId: '', productoId: '', cantidad: '', tipo: 'NORMAL', precioUnitario: '',
};

function NuevoPedido() {
  const [form, setForm]       = useState(FORM_INICIAL);
  const [loading, setLoading] = useState(false);
  const [exito, setExito]     = useState(null);
  const [error, setError]     = useState(null);

  const handleChange = e => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setExito(null);
    setError(null);
  };

  // Calcular precio estimado según tipo
  const calcularPrecioEstimado = () => {
    const precio   = parseFloat(form.precioUnitario) || 0;
    const cantidad = parseInt(form.cantidad) || 0;
    const base = precio * cantidad;
    if (form.tipo === 'URGENTE')   return (base * 1.15).toFixed(2);
    if (form.tipo === 'MAYORISTA') return (base * 0.80).toFixed(2);
    return base.toFixed(2);
  };

  const handleSubmit = async () => {
    const { clienteId, productoId, cantidad, tipo, precioUnitario } = form;
    if (!clienteId || !productoId || !cantidad || !precioUnitario) {
      setError('Por favor completa todos los campos.');
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const resultado = await crearPedido({
        clienteId,
        productoId: parseInt(productoId),
        cantidad: parseInt(cantidad),
        tipo,
        precioUnitario: parseFloat(precioUnitario),
      });
      setExito(`✅ Pedido #${resultado.id} creado exitosamente. Total: $${resultado.precioTotal?.toFixed(2)}`);
      setForm(FORM_INICIAL);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  const precioEstimado = calcularPrecioEstimado();

  return (
    <div>
      <h1 className="page-title">
        Nuevo Pedido
        <div className="page-subtitle">Registra un pedido en el sistema SmartLogix</div>
      </h1>

      <div className="section">
        <h2 className="section-title">Datos del Pedido</h2>

        {exito && <div className="alert alert-success">{exito}</div>}
        {error && <div className="alert alert-error">⚠️ {error}</div>}

        <div className="form-grid">
          <div className="form-group">
            <label>ID Cliente *</label>
            <input name="clienteId" placeholder="Ej: CLI-001" value={form.clienteId} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>ID Producto *</label>
            <input name="productoId" type="number" min="1" placeholder="Ej: 1" value={form.productoId} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Cantidad *</label>
            <input name="cantidad" type="number" min="1" placeholder="Ej: 5" value={form.cantidad} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Precio Unitario ($) *</label>
            <input name="precioUnitario" type="number" min="0" step="0.01" placeholder="Ej: 99.99" value={form.precioUnitario} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Tipo de Pedido *</label>
            <select name="tipo" value={form.tipo} onChange={handleChange}>
              {TIPOS_PEDIDO.map(t => (
                <option key={t.value} value={t.value}>{t.label}</option>
              ))}
            </select>
          </div>
        </div>

        {/* Resumen estimado */}
        {form.cantidad && form.precioUnitario && (
          <div className="alert alert-success" style={{ marginBottom: '1.5rem' }}>
            💰 <strong>Precio estimado:</strong> ${precioEstimado}
            {form.tipo === 'URGENTE'   && ' (incluye recargo del 15%)'}
            {form.tipo === 'MAYORISTA' && ' (incluye descuento del 20%)'}
          </div>
        )}

        <button className="btn btn-primary" onClick={handleSubmit} disabled={loading}>
          {loading ? 'Creando pedido...' : '📦 Crear Pedido'}
        </button>
      </div>

      {/* Referencia tipos */}
      <div className="section">
        <h2 className="section-title">Referencia de Tipos de Pedido</h2>
        <div className="table-wrapper">
          <table>
            <thead>
              <tr><th>Tipo</th><th>Días de Entrega</th><th>Precio</th><th>Uso recomendado</th></tr>
            </thead>
            <tbody>
              <tr>
                <td><span className="badge badge-normal">NORMAL</span></td>
                <td>5 días</td><td>Sin recargo</td>
                <td>Pedidos estándar de baja urgencia</td>
              </tr>
              <tr>
                <td><span className="badge badge-urgente">URGENTE</span></td>
                <td>1 día</td><td>+15%</td>
                <td>Entregas express o picos de demanda</td>
              </tr>
              <tr>
                <td><span className="badge badge-mayorista">MAYORISTA</span></td>
                <td>7 días</td><td>-20%</td>
                <td>Compras en volumen con descuento</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default NuevoPedido;
