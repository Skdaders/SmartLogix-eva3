package cl.duoc.smartlogix.bff.controller;

import cl.duoc.smartlogix.bff.dto.BffDTO;
import cl.duoc.smartlogix.bff.service.BffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor
@Tag(name = "BFF SmartLogix", description = "Backend For Frontend — agrega datos para el frontend React")
@CrossOrigin(origins = "*")
public class BffController {

    private final BffService bffService;

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard consolidado: bajo stock + pedidos recientes")
    public ResponseEntity<BffDTO.DashboardResponse> getDashboard() {
        return ResponseEntity.ok(bffService.getDashboard());
    }

    @GetMapping("/productos")
    @Operation(summary = "Listar todos los productos del inventario")
    public ResponseEntity<List<Map<String, Object>>> getProductos() {
        return ResponseEntity.ok(bffService.getProductos());
    }

    @GetMapping("/productos/{id}")
    @Operation(summary = "Detalle de producto con stock actual")
    public ResponseEntity<BffDTO.ProductoDetalleResponse> getProductoDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.getProductoDetalle(id));
    }

    @PostMapping("/pedidos")
    @Operation(summary = "Crear nuevo pedido (proxy a ms-pedidos)")
    public ResponseEntity<Map<String, Object>> crearPedido(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(bffService.crearPedido(request));
    }

    @GetMapping("/pedidos/cliente/{clienteId}")
    @Operation(summary = "Pedidos de un cliente específico")
    public ResponseEntity<List<Map<String, Object>>> getPedidosPorCliente(@PathVariable String clienteId) {
        return ResponseEntity.ok(bffService.getPedidosPorCliente(clienteId));
    }
}
