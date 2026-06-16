package cl.duoc.smartlogix.pedidos.factory;

import cl.duoc.smartlogix.pedidos.model.Pedido;
import java.time.LocalDateTime;

// ── Pedido Normal: 5 días, sin recargo ───────────────────────────────────────
class PedidoNormalFactory extends PedidoFactory {
    @Override
    public Pedido crearPedido(String clienteId, Long productoId,
                              Integer cantidad, Double precioUnitario) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId);
        pedido.setProductoId(productoId);
        pedido.setCantidad(cantidad);
        pedido.setTipo("NORMAL");
        pedido.setPrecioTotal(precioUnitario * cantidad);       // sin recargo
        pedido.setDiasEntrega(5);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        return pedido;
    }
}

// ── Pedido Urgente: 1 día, +15% recargo ──────────────────────────────────────
class PedidoUrgenteFactory extends PedidoFactory {
    @Override
    public Pedido crearPedido(String clienteId, Long productoId,
                              Integer cantidad, Double precioUnitario) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId);
        pedido.setProductoId(productoId);
        pedido.setCantidad(cantidad);
        pedido.setTipo("URGENTE");
        pedido.setPrecioTotal(precioUnitario * cantidad * 1.15); // +15%
        pedido.setDiasEntrega(1);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        return pedido;
    }
}

// ── Pedido Mayorista: 7 días, -20% descuento ─────────────────────────────────
class PedidoMayoristaFactory extends PedidoFactory {
    @Override
    public Pedido crearPedido(String clienteId, Long productoId,
                              Integer cantidad, Double precioUnitario) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId);
        pedido.setProductoId(productoId);
        pedido.setCantidad(cantidad);
        pedido.setTipo("MAYORISTA");
        pedido.setPrecioTotal(precioUnitario * cantidad * 0.80); // -20%
        pedido.setDiasEntrega(7);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        return pedido;
    }
}
