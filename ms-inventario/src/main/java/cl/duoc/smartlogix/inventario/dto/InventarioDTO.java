package cl.duoc.smartlogix.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InventarioDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoRequest {
        private String codigo;
        private String nombre;
        private Integer stockActual;
        private Integer stockMinimo;
        private Long bodegaId;
        private Double precioUnitario;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoResponse {
        private Long id;
        private String codigo;
        private String nombre;
        private Integer stockActual;
        private Integer stockMinimo;
        private Long bodegaId;
        private Double precioUnitario;
        private Boolean bajoStock;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockUpdateRequest {
        private Long productoId;
        private Integer cantidad;
        private String operacion; // "ENTRADA" o "SALIDA"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockResponse {
        private Long productoId;
        private Integer stockActual;
        private String mensaje;
    }
}
