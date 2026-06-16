package cl.duoc.smartlogix.pedidos.dto;

import cl.duoc.smartlogix.pedidos.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class PedidoDTO {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class PedidoRequest {
        private String clienteId;
        private Long productoId;
        private Integer cantidad;
        private String tipo;        // NORMAL, URGENTE, MAYORISTA
        private Double precioUnitario;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
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
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CambioEstadoRequest {
        private Pedido.EstadoPedido nuevoEstado;
    }
}
