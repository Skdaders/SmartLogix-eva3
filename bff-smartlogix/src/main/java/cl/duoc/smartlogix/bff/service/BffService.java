package cl.duoc.smartlogix.bff.service;

import cl.duoc.smartlogix.bff.dto.BffDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Patrón Backend For Frontend (BFF):
 * Agrega y adapta las respuestas de ms-inventario y ms-pedidos
 * al formato exacto que necesita el frontend React.
 */
@Service
@RequiredArgsConstructor
public class BffService {

    private final RestTemplate restTemplate;

    @Value("${smartlogix.ms-inventario.url}")
    private String inventarioUrl;

    @Value("${smartlogix.ms-pedidos.url}")
    private String pedidosUrl;

    // ── Dashboard consolidado para el frontend ────────────────────────────
    public BffDTO.DashboardResponse getDashboard() {
        // Consumir ms-inventario
        List<Map<String, Object>> bajoStock = getFromInventario("/api/inventario/bajo-stock");
        List<Map<String, Object>> productos  = getFromInventario("/api/inventario/productos");

        // Consumir ms-pedidos
        List<Map<String, Object>> pedidos = getFromPedidos("/api/pedidos");

        return new BffDTO.DashboardResponse(
                bajoStock,
                pedidos,
                productos != null ? productos.size() : 0,
                pedidos  != null ? pedidos.size()   : 0
        );
    }

    // ── Detalle de producto con stock ─────────────────────────────────────
    public BffDTO.ProductoDetalleResponse getProductoDetalle(Long productoId) {
        Map<String, Object> producto = restTemplate.getForObject(
                inventarioUrl + "/api/inventario/productos/" + productoId, Map.class);
        Map<String, Object> stock = restTemplate.getForObject(
                inventarioUrl + "/api/inventario/stock/" + productoId, Map.class);
        return new BffDTO.ProductoDetalleResponse(producto, stock);
    }

    // ── Crear pedido (proxy al ms-pedidos) ────────────────────────────────
    public Map<String, Object> crearPedido(Map<String, Object> pedidoRequest) {
        return restTemplate.postForObject(
                pedidosUrl + "/api/pedidos", pedidoRequest, Map.class);
    }

    // ── Listar todos los productos ────────────────────────────────────────
    public List<Map<String, Object>> getProductos() {
        return getFromInventario("/api/inventario/productos");
    }

    // ── Listar pedidos de un cliente ──────────────────────────────────────
    public List<Map<String, Object>> getPedidosPorCliente(String clienteId) {
        return getFromPedidos("/api/pedidos/cliente/" + clienteId);
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getFromInventario(String path) {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    inventarioUrl + path,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {});
            return response.getBody();
        } catch (Exception e) {
            return List.of();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getFromPedidos(String path) {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    pedidosUrl + path,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {});
            return response.getBody();
        } catch (Exception e) {
            return List.of();
        }
    }
}
