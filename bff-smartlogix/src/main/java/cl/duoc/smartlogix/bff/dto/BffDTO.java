package cl.duoc.smartlogix.bff.dto;

import java.util.List;
import java.util.Map;

public class BffDTO {

    public static class DashboardResponse {
        private List<Map<String, Object>> productosBajoStock;
        private List<Map<String, Object>> pedidosRecientes;
        private Integer totalProductos;
        private Integer totalPedidos;

        public DashboardResponse() {}
        public DashboardResponse(List<Map<String, Object>> productosBajoStock,
                                 List<Map<String, Object>> pedidosRecientes,
                                 Integer totalProductos, Integer totalPedidos) {
            this.productosBajoStock = productosBajoStock;
            this.pedidosRecientes = pedidosRecientes;
            this.totalProductos = totalProductos;
            this.totalPedidos = totalPedidos;
        }
        public List<Map<String, Object>> getProductosBajoStock() { return productosBajoStock; }
        public void setProductosBajoStock(List<Map<String, Object>> productosBajoStock) { this.productosBajoStock = productosBajoStock; }
        public List<Map<String, Object>> getPedidosRecientes() { return pedidosRecientes; }
        public void setPedidosRecientes(List<Map<String, Object>> pedidosRecientes) { this.pedidosRecientes = pedidosRecientes; }
        public Integer getTotalProductos() { return totalProductos; }
        public void setTotalProductos(Integer totalProductos) { this.totalProductos = totalProductos; }
        public Integer getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(Integer totalPedidos) { this.totalPedidos = totalPedidos; }
    }

    public static class ProductoDetalleResponse {
        private Map<String, Object> producto;
        private Map<String, Object> stock;

        public ProductoDetalleResponse() {}
        public ProductoDetalleResponse(Map<String, Object> producto, Map<String, Object> stock) {
            this.producto = producto;
            this.stock = stock;
        }
        public Map<String, Object> getProducto() { return producto; }
        public void setProducto(Map<String, Object> producto) { this.producto = producto; }
        public Map<String, Object> getStock() { return stock; }
        public void setStock(Map<String, Object> stock) { this.stock = stock; }
    }
}