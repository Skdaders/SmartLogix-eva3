package cl.duoc.smartlogix.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

public class BffDTO {

    /** Vista consolidada para el dashboard del frontend */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DashboardResponse {
        private List<Map<String, Object>> productosBajoStock;
        private List<Map<String, Object>> pedidosRecientes;
        private Integer totalProductos;
        private Integer totalPedidos;
    }

    /** Detalle de producto con su stock y pedidos asociados */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ProductoDetalleResponse {
        private Map<String, Object> producto;
        private Map<String, Object> stock;
    }
}
