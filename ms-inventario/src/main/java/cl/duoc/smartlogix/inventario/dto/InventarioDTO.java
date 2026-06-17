package cl.duoc.smartlogix.inventario.dto;

public class InventarioDTO {

    public static class ProductoRequest {
        private String codigo;
        private String nombre;
        private Integer stockActual;
        private Integer stockMinimo;
        private Long bodegaId;
        private Double precioUnitario;

        public ProductoRequest() {}

        public ProductoRequest(String codigo, String nombre, Integer stockActual,
                               Integer stockMinimo, Long bodegaId, Double precioUnitario) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.stockActual = stockActual;
            this.stockMinimo = stockMinimo;
            this.bodegaId = bodegaId;
            this.precioUnitario = precioUnitario;
        }

        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getStockActual() { return stockActual; }
        public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
        public Integer getStockMinimo() { return stockMinimo; }
        public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
        public Long getBodegaId() { return bodegaId; }
        public void setBodegaId(Long bodegaId) { this.bodegaId = bodegaId; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    }

    public static class ProductoResponse {
        private Long id;
        private String codigo;
        private String nombre;
        private Integer stockActual;
        private Integer stockMinimo;
        private Long bodegaId;
        private Double precioUnitario;
        private Boolean bajoStock;

        public ProductoResponse() {}

        public ProductoResponse(Long id, String codigo, String nombre, Integer stockActual,
                                Integer stockMinimo, Long bodegaId, Double precioUnitario, Boolean bajoStock) {
            this.id = id; this.codigo = codigo; this.nombre = nombre;
            this.stockActual = stockActual; this.stockMinimo = stockMinimo;
            this.bodegaId = bodegaId; this.precioUnitario = precioUnitario;
            this.bajoStock = bajoStock;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getStockActual() { return stockActual; }
        public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
        public Integer getStockMinimo() { return stockMinimo; }
        public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
        public Long getBodegaId() { return bodegaId; }
        public void setBodegaId(Long bodegaId) { this.bodegaId = bodegaId; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
        public Boolean getBajoStock() { return bajoStock; }
        public void setBajoStock(Boolean bajoStock) { this.bajoStock = bajoStock; }
    }

    public static class StockUpdateRequest {
        private Long productoId;
        private Integer cantidad;
        private String operacion;

        public StockUpdateRequest() {}

        public StockUpdateRequest(Long productoId, Integer cantidad, String operacion) {
            this.productoId = productoId;
            this.cantidad = cantidad;
            this.operacion = operacion;
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public String getOperacion() { return operacion; }
        public void setOperacion(String operacion) { this.operacion = operacion; }
    }

    public static class StockResponse {
        private Long productoId;
        private Integer stockActual;
        private String mensaje;

        public StockResponse() {}

        public StockResponse(Long productoId, Integer stockActual, String mensaje) {
            this.productoId = productoId;
            this.stockActual = stockActual;
            this.mensaje = mensaje;
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getStockActual() { return stockActual; }
        public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}