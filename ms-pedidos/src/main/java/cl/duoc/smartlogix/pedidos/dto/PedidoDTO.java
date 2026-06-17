package cl.duoc.smartlogix.pedidos.dto;

import cl.duoc.smartlogix.pedidos.model.Pedido;
import java.time.LocalDateTime;

public class PedidoDTO {

    public static class PedidoRequest {
        private String clienteId;
        private Long productoId;
        private Integer cantidad;
        private String tipo;
        private Double precioUnitario;

        public PedidoRequest() {}
        public PedidoRequest(String clienteId, Long productoId, Integer cantidad,
                             String tipo, Double precioUnitario) {
            this.clienteId = clienteId;
            this.productoId = productoId;
            this.cantidad = cantidad;
            this.tipo = tipo;
            this.precioUnitario = precioUnitario;
        }
        public String getClienteId() { return clienteId; }
        public void setClienteId(String clienteId) { this.clienteId = clienteId; }
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    }

    public static class PedidoResponse {
        private Long id;
        private String clienteId;
        private Long productoId;
        private Integer cantidad;
        private String tipo;
        private Double precioTotal;
        private Integer diasEntrega;
        private String estado;
        private LocalDateTime fechaCreacion;

        public PedidoResponse() {}
        public PedidoResponse(Long id, String clienteId, Long productoId, Integer cantidad,
                              String tipo, Double precioTotal, Integer diasEntrega,
                              String estado, LocalDateTime fechaCreacion) {
            this.id = id; this.clienteId = clienteId; this.productoId = productoId;
            this.cantidad = cantidad; this.tipo = tipo; this.precioTotal = precioTotal;
            this.diasEntrega = diasEntrega; this.estado = estado;
            this.fechaCreacion = fechaCreacion;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getClienteId() { return clienteId; }
        public void setClienteId(String clienteId) { this.clienteId = clienteId; }
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public Double getPrecioTotal() { return precioTotal; }
        public void setPrecioTotal(Double precioTotal) { this.precioTotal = precioTotal; }
        public Integer getDiasEntrega() { return diasEntrega; }
        public void setDiasEntrega(Integer diasEntrega) { this.diasEntrega = diasEntrega; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public LocalDateTime getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    }

    public static class CambioEstadoRequest {
        private Pedido.EstadoPedido nuevoEstado;

        public CambioEstadoRequest() {}
        public CambioEstadoRequest(Pedido.EstadoPedido nuevoEstado) {
            this.nuevoEstado = nuevoEstado;
        }
        public Pedido.EstadoPedido getNuevoEstado() { return nuevoEstado; }
        public void setNuevoEstado(Pedido.EstadoPedido nuevoEstado) { this.nuevoEstado = nuevoEstado; }
    }
}